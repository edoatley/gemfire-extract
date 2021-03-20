package com.edoatley.gemfireextract;

import com.edoatley.gemfireextract.model.Repository;
import com.edoatley.gemfireextract.model.RepositorySecurityScore;
import com.edoatley.gemfireextract.model.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GemfireExtractApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

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
		Files.lines(Path.of(new ClassPathResource("src/test/resources/generated-data.txt").getPath()))
				.map(s -> restTemplate.exchange("/repositories", HttpMethod.POST, new HttpEntity<>(s), Repository.class, emptyMap()));

		final ResponseEntity<String> response = restTemplate.getForEntity("/security", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		System.err.println(response.getBody());
	}
}
