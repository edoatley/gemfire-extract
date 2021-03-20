package com.edoatley.gemfireextract.model;

import org.apache.geode.cache.query.internal.StructImpl;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StructImplToRepositorySecurityScoreConverter implements Converter<StructImpl, RepositorySecurityScore> {

    @Override
    public RepositorySecurityScore convert(StructImpl source) {
        final Object criticals = source.get("criticals");
        final Object severe = source.get("severe");
        final Object moderate = source.get("moderate");
        return new RepositorySecurityScore(criticals.toString(), severe.toString(), moderate.toString());
    }
}
