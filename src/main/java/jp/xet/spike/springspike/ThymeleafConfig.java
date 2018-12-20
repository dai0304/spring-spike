/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.xet.spike.springspike;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import jp.xet.spike.springspike.thymeleaf.AmazonS3TemplateResolver;
import jp.xet.springconfig.aws.v1.EnableAwsClientV1;

/**
 * Spring thymeleaf Configuration.
 */
@Configuration
@RequiredArgsConstructor
@EnableAwsClientV1(AmazonS3.class)
public class ThymeleafConfig {
	
	private final AmazonS3 amazonS3;
	
	@Bean
	public AmazonS3TemplateResolver resolver() {
		AmazonS3TemplateResolver resolver = new AmazonS3TemplateResolver(amazonS3, "cm-miyamoto-test");
		resolver.setPrefix("templates/");
		resolver.setSuffix(".html");
		resolver.setCheckExistence(true);
		resolver.setOrder(1);
		return resolver;
	}
}
