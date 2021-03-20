package com.edoatley.gemfireextract.config;

import com.edoatley.gemfireextract.model.StructImplToRepositorySecurityScoreConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.convert.CustomConversions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AddCustomConverters {
//    @Bean
//    CustomConversions customConversions() {
//        List<Converter<?, ?>> converters = new ArrayList<>();
//        converters.add(new StructImplToRepositorySecurityScoreConverter());
//        CustomConversions.StoreConversions storeConversions= CustomConversions.StoreConversions.NONE;
//        return new CustomConversions(storeConversions, converters);
//    }
    @Bean
    public ConversionServiceFactoryBean conversionService(Set<Converter<?, ?>> converters) {
        ConversionServiceFactoryBean factory = new ConversionServiceFactoryBean();
        System.err.println(converters.stream().map(Objects::toString).collect(Collectors.joining(" ")));
        factory.setConverters(converters);
        return factory;
    }
}
