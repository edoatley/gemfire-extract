package com.edoatley.gemfireextract.config;

import com.edoatley.gemfireextract.model.Repository;
import org.apache.geode.pdx.PdxSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.mapping.MappingPdxSerializer;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.geode.config.annotation.EnableClusterAware;

@Configuration
@EnablePdx
@EnableClusterAware
@EnableEntityDefinedRegions(basePackageClasses = Repository.class)
public class CoreGemfireConfiguration {

    @Bean
    PdxSerializer pdxSerializer() {
        return new MappingPdxSerializer();
    }
}
