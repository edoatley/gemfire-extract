package com.edoatley.gemfireextract;

import com.edoatley.gemfireextract.data.RepositoryRepository;
import com.edoatley.gemfireextract.model.Repository;
import com.edoatley.gemfireextract.model.RepositorySecurityScore;
import com.edoatley.gemfireextract.model.Tag;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.lingala.zip4j.ZipFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GemfireExtractApplicationTests {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Value("classpath:generated-data.txt")
	Resource generatedFile;
	public static final String GENERATED_DATA_ZIP = "generated-data.zip";

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	RepositoryRepository repository;

	@BeforeEach
	void setup() {
		repository.deleteAll();
	}

	@Test
	void contextLoads() {}

	@Test
	void saveARepoThenRetrieve() {
		Repository repo = Repository.builder()
				.url("https://github.com/MyBestApp")
				.app("MyBestApp")
				.rating("TheBest")
				.tags(List.of(new Tag("java"), new Tag("gemfire")))
				.repositorySecurityScore(RepositorySecurityScore.builder().criticals("1").severe("2").moderate("0").build())
				.build();
		HttpEntity<Repository> entity = new HttpEntity<>(repo);

		final ResponseEntity<Repository> saved = restTemplate.exchange("/repositories", HttpMethod.POST, entity, Repository.class, emptyMap());

		final URI uri = UriComponentsBuilder.fromPath("/repositories/{id}").build(saved.getBody().getId());
		final ResponseEntity<Repository> response = restTemplate.getForEntity(uri, Repository.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(repo);
	}

	@Test
	void loadSomeDataThenFindTheSecurityDetails() throws IOException {
		final List<String> lines = Files.lines(Path.of(generatedFile.getFile().getPath()))
				.collect(Collectors.toList());
		lines.forEach(s -> {
			System.err.println(s);
			try {
				final HttpEntity<Repository> requestEntity = new HttpEntity<>(objectMapper.readValue(s, Repository.class));
				final ResponseEntity<Repository> res = restTemplate.exchange("/repositories", HttpMethod.POST, requestEntity, Repository.class, emptyMap());
				assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});

		assertThat(repository.count()).isEqualTo(10);

		final ResponseEntity<String> response = restTemplate.getForEntity("/security", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		System.err.println(response.getBody());
	}

	@Test
	void loadSomeDataFromZipAndReadIt() throws IOException {
		File sourceText = generatedFile.getFile();
		File sourceZip = new File(GENERATED_DATA_ZIP);
		ZipFile zipFile = new ZipFile(sourceZip);
		zipFile.addFile(sourceText);

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();
		requestMap.add("user-file", new FileSystemResource(sourceZip));

		final ResponseEntity<String> saveResponse = restTemplate.exchange("/upload", HttpMethod.POST, new HttpEntity<>(requestMap, headers), String.class);
		assertThat(saveResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		final URI uri = UriComponentsBuilder.fromPath("/repositories/{id}").build("25FE69C3-06C2-491D-AFC5-14D496372796");
		final ResponseEntity<Repository> response = restTemplate.getForEntity(uri, Repository.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getId()).isEqualTo("25FE69C3-06C2-491D-AFC5-14D496372796");
	}

}
