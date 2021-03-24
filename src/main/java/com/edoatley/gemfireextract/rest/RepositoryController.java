package com.edoatley.gemfireextract.rest;

import com.edoatley.gemfireextract.model.Repository;
import com.edoatley.gemfireextract.model.RepositorySecurityScore;
import com.edoatley.gemfireextract.service.RepositoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RepositoryController {

    private final RepositoryService repositoryService;

    @PostMapping(value = "/repositories",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Repository> saveRepository(@RequestBody Repository repository) {
        Repository saved = repositoryService.saveRepository(repository);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/repositories/{id}")
    public ResponseEntity<Repository> findById(@PathVariable("id") String id) {
        return ResponseEntity.of(repositoryService.findRepositoryById(id));
    }

    @GetMapping("/security")
    public ResponseEntity<List<RepositorySecurityScore>> getSecurityScores() {
        return ResponseEntity.ok(repositoryService.getSecurityScores());
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("user-file") MultipartFile multipart) throws IOException {
        final Path tempFile = Files.createTempFile("zip-upload",  null);
        multipart.transferTo(tempFile);
        ZipFile zipFile = new ZipFile(tempFile.toFile());
        FileHeader fileHeader = zipFile.getFileHeader("generated-data.txt");
        try(InputStream inputStream = zipFile.getInputStream(fileHeader)){
            LineNumberReader lnr = new LineNumberReader(new InputStreamReader(inputStream));
            lnr.lines().forEach(s -> {
                System.err.println(s);
                ObjectMapper mapper = new ObjectMapper();
                Repository r = null;
                try {
                    r = mapper.readValue(s, Repository.class);
                    repositoryService.saveRepository(r);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
            return ResponseEntity.ok("Accepted");
        } catch (IOException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }
}
