package com.edoatley.gemfireextract.data;

import com.edoatley.gemfireextract.model.Repository;
import org.springframework.data.repository.CrudRepository;

@org.springframework.stereotype.Repository
public interface RepositoryRepository extends CrudRepository<Repository, String> {
}
