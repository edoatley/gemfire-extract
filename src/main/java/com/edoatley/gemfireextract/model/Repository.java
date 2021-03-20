package com.edoatley.gemfireextract.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

import java.util.List;

@Data
@Builder
@Region("repos")
@NoArgsConstructor
@AllArgsConstructor
public class Repository {
    @Id
    String id;
    String url;
    String app;
    String rating;
    List<Tag> tags;
    RepositorySecurityScore repositorySecurityScore;
}
