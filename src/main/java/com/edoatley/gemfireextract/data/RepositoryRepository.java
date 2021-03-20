package com.edoatley.gemfireextract.data;

import com.edoatley.gemfireextract.model.Repository;
import com.edoatley.gemfireextract.model.RepositorySecurityScore;
import com.edoatley.gemfireextract.model.Tag;
import org.springframework.data.gemfire.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@org.springframework.stereotype.Repository
public interface RepositoryRepository extends CrudRepository<Repository, String> {

    @Query("select distinct s.criticals, s.severe, s.moderate " +
            "from /repos r, r.repositorySecurityScore s")
    List<RepositorySecurityScore> getSecurityScores();

    @Query("select distinct t.value " +
            "from /repos r, t.tags s")
    List<Tag> getTags();
}
