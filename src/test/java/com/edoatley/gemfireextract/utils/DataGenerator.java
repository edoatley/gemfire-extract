package com.edoatley.gemfireextract.utils;

import com.edoatley.gemfireextract.model.Repository;
import com.edoatley.gemfireextract.model.RepositorySecurityScore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class DataGenerator {

    private static final String inputFile = "/Users/edoatley/code/gemfire-extract/src/test/resources/repositories.csv";
    private static Path outputFile = Path.of("/Users/edoatley/code/gemfire-extract/src/test/resources/generated-data.txt");
    private static ObjectMapper mapper;

    @BeforeAll
    static void setupOutput() throws IOException {
        Files.deleteIfExists(outputFile);
        outputFile = Files.createFile(outputFile);
        mapper = new ObjectMapper();
    }

    @ParameterizedTest
    @CsvFileSource(files = {inputFile}, numLinesToSkip = 1)
    void generateJson(String id, String url, String app, String rating, String tags, String criticals, String severe,
                      String moderate) throws IOException {

        Repository repo = Repository.builder()
                .id(id)
                .url(url)
                .app(app)
                .rating(rating)
                .tags(Arrays.asList(tags.split("\\|").clone()))
                .repositorySecurityScore(RepositorySecurityScore.builder()
                        .criticals(criticals)
                        .severe(severe)
                        .moderate(moderate)
                        .build())
                .build();
        Files.writeString(outputFile, mapper.writeValueAsString(repo) + "\n", StandardCharsets.UTF_8, StandardOpenOption.APPEND);
    }
}