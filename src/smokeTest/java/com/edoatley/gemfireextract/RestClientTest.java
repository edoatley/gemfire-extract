package com.edoatley.gemfireextract;

import com.edoatley.gemfireextract.model.Repository;
import com.edoatley.gemfireextract.model.RepositorySecurityScore;
import com.edoatley.gemfireextract.model.Tag;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

public class RestClientTest {

    private static final String HOSTNAME_PROPERTY = "HOSTNAME_VAR";
    private static final String DEPENDENCIES_PATH = "/dependencies";
    private static final String DEPENDENCIES_BYID_PATH = "/dependencies/{id}";
    static RestTemplate restTemplate;
    static ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        String hostname = System.getProperty(HOSTNAME_PROPERTY);
        restTemplate = new RestTemplateBuilder()
                .rootUri(hostname)
                .build();
    }

    @Test
    @DisplayName("Save a basic repository and retrieve it")
    public void saveAndRetrieve() {

        // Given
        Repository repo = Repository.builder()
                .url("https://github.com/MyBestApp")
                .app("MyBestApp")
                .rating("TheBest")
                .tags(List.of(new Tag("java"), new Tag("gemfire")))
                .repositorySecurityScore(RepositorySecurityScore.builder().criticals("1").severe("2").moderate("0").build())
                .build();
        HttpEntity<Repository> entity = new HttpEntity<>(repo);

        final ResponseEntity<Repository> saved = restTemplate.exchange(DEPENDENCIES_PATH, HttpMethod.POST, entity, Repository.class, emptyMap());

        final URI uri = UriComponentsBuilder.fromPath(DEPENDENCIES_BYID_PATH).build(saved.getBody().getId());
        final ResponseEntity<Repository> response = restTemplate.getForEntity(uri, Repository.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(repo);

    }
}
