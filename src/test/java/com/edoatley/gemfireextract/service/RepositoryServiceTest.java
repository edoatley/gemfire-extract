package com.edoatley.gemfireextract.service;

import com.edoatley.gemfireextract.data.RepositoryRepository;
import com.edoatley.gemfireextract.model.Repository;
import com.edoatley.gemfireextract.model.RepositorySecurityScore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepositoryServiceTest {

    @Mock
    RepositoryRepository repository;

    @InjectMocks
    RepositoryService repositoryService;

    @Captor
    ArgumentCaptor<Repository> captor;

    Repository dummyRepo = Repository.builder()
            .id("D26AE342-BCEC-46F9-B648-28F2C28E139D")
            .url("https://repositories-are-us.com")
            .app("Diablo IV")
            .tags(List.of("game", "devil", "ARPG"))
            .rating("Epic")
            .repositorySecurityScore(RepositorySecurityScore.builder().criticals("0").severe("3").moderate("5").build())
            .build();

    Repository dummyRepoWithoutId = Repository.builder()
            .url("https://repositories-are-us.com")
            .app("Diablo IV")
            .tags(List.of("game", "devil", "ARPG"))
            .rating("Epic")
            .repositorySecurityScore(RepositorySecurityScore.builder().criticals("0").severe("3").moderate("5").build())
            .build();

    @Test
    void nullIdPopulatedBeforeRepositoryCall() {
        when(repository.save(captor.capture())).thenReturn(dummyRepo);
        final Repository savedValue = repositoryService.saveRepository(dummyRepoWithoutId);
        assertThat(savedValue).usingRecursiveComparison().ignoringFields("id").isEqualTo(dummyRepo);
        assertThat(captor.getValue().getId()).isNotNull();
    }

    @Test
    void overwriteRepositoryUsingSameId() {
        when(repository.save(captor.capture())).thenReturn(dummyRepo);
        final Repository savedValue = repositoryService.saveRepository(dummyRepo);
        assertThat(savedValue).usingRecursiveComparison().isEqualTo(dummyRepo);
        assertThat(captor.getValue().getId()).isEqualTo(dummyRepo.getId());
    }
}