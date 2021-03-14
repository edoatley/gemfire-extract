package com.edoatley.gemfireextract.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RepositorySecurityScore {
    String criticals;
    String severe;
    String moderate;
}
