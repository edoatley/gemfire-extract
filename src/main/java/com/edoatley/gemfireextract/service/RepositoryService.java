package com.edoatley.gemfireextract.service;

import com.edoatley.gemfireextract.data.RepositoryRepository;
import com.edoatley.gemfireextract.model.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RepositoryService {
    private final RepositoryRepository repositoryRepository;

    public Repository saveRepository(Repository repository) {
        if (repository.getId() == null) {
           repository.setId(UUID.randomUUID().toString());
        }
        return repositoryRepository.save(repository);
    }

    public Optional<Repository> findRepositoryById(String id) {
        return repositoryRepository.findById(id);
    }
}
