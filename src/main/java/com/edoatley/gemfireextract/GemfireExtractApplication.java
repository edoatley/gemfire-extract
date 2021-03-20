package com.edoatley.gemfireextract;

import com.edoatley.gemfireextract.model.StructImplToRepositorySecurityScoreConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GemfireExtractApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(GemfireExtractApplication.class, args);
		StructImplToRepositorySecurityScoreConverter bean = context.getBean(StructImplToRepositorySecurityScoreConverter.class);
		System.out.println(bean);
	}

}
