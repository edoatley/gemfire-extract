package com.edoatley.gemfireextract.rest;

import com.edoatley.gemfireextract.data.RepositoryRepository;
import com.edoatley.gemfireextract.model.Repository;
import com.edoatley.gemfireextract.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
