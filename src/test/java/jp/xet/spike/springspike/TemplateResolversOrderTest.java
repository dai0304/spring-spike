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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import jp.xet.spike.springspike.thymeleaf.AmazonS3TemplateResolver;

/**
 * TODO miyamoto.daisuke.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TemplateResolversOrderTest {
	
	@Autowired
	SpringTemplateEngine engine;
	
	@Test
	public void verifyTemplateResolverOrder() {
		Set<ITemplateResolver> templateResolvers = engine.getTemplateResolvers();
		assertThat(templateResolvers).isNotNull().hasSize(2);
		
		Iterator<ITemplateResolver> iterator = templateResolvers.iterator();
		ITemplateResolver s3Resolver = iterator.next();
		assertThat(s3Resolver.getOrder()).isEqualTo(1);
		assertThat(s3Resolver.getName()).isEqualTo(AmazonS3TemplateResolver.class.getName());
		
		ITemplateResolver springResolver = iterator.next();
		assertThat(springResolver.getOrder()).isNull();
		assertThat(springResolver.getName()).isEqualTo(SpringResourceTemplateResolver.class.getName()); 
	}
}
