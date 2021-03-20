package com.edoatley.gemfireextract.rest;

import com.edoatley.gemfireextract.model.Repository;
import com.edoatley.gemfireextract.model.RepositorySecurityScore;
import com.edoatley.gemfireextract.model.Tag;
import com.edoatley.gemfireextract.service.RepositoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class RepositoryControllerTest {
    public static final String ID = "ID";
    public static final String BAD_ID = "BADID";
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RepositoryService repositoryService;

    @Test
    void canRetrieveById() throws Exception {
        when(repositoryService.findRepositoryById(eq(ID))).thenReturn(Optional.of(simpleRepoWithId()));

        this.mockMvc.perform(get("/repositories/" + ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(simpleRepoWithId())));
    }

    @Test
    void canReturnNotFoundIfDoesNotExist() throws Exception {
        when(repositoryService.findRepositoryById(eq(BAD_ID))).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/repositories/" + BAD_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void canSaveARepository() throws Exception {
        String content = mapper.writeValueAsString(simpleRepo().build());
        when(repositoryService.saveRepository(eq(simpleRepo().build()))).thenReturn(simpleRepoWithId());

        this.mockMvc.perform(post("/repositories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(simpleRepoWithId())));
    }

    private Repository.RepositoryBuilder simpleRepo() {
        return Repository.builder()
                .url("https://github.com/MyFirstApp")
                .app("MyFirstApp")
                .rating("Awesome")
                .tags(List.of("java", "docker", "gemfire").stream().map(Tag::new).collect(Collectors.toList()))
                .repositorySecurityScore(RepositorySecurityScore.builder()
                        .criticals("0")
                        .severe("0")
                        .moderate("1")
                        .build());
    }

    private Repository simpleRepoWithId() {
        return simpleRepo().id(ID).build();
    }
}