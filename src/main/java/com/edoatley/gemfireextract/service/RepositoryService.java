package com.edoatley.gemfireextract.service;

import com.edoatley.gemfireextract.data.RepositoryRepository;
import com.edoatley.gemfireextract.model.Repository;
import com.edoatley.gemfireextract.model.RepositorySecurityScore;
import com.edoatley.gemfireextract.model.StructImplToRepositorySecurityScoreConverter;
import lombok.RequiredArgsConstructor;
import org.apache.geode.cache.query.internal.StructImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepositoryService {
    private final RepositoryRepository repositoryRepository;
    private final StructImplToRepositorySecurityScoreConverter converter;

    public Repository saveRepository(Repository repository) {
        if (repository.getId() == null) {
           repository.setId(UUID.randomUUID().toString());
        }
        return repositoryRepository.save(repository);
    }

    public Optional<Repository> findRepositoryById(String id) {
        return repositoryRepository.findById(id);
    }

    public List<RepositorySecurityScore> getSecurityScores() {
        List<StructImpl> result = repositoryRepository.getSecurityScores();
        return result.stream().map(converter::convert)
                .collect(Collectors.toList());
    }
}
