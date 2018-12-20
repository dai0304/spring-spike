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
package jp.xet.spike.springspike.thymeleaf;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.spring5.templateresource.SpringResourceTemplateResource;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

import com.amazonaws.services.s3.AmazonS3;

/**
 * {@link org.thymeleaf.templateresolver.ITemplateResolver} implementation to load template from Amazon S3 bucket.
 */
@Slf4j
@RequiredArgsConstructor
public class AmazonS3TemplateResolver extends AbstractConfigurableTemplateResolver {
	
	private final AmazonS3 amazonS3;
	
	private final String bucketName;
	
	@Override
	protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate,
			String template, String resourceName, String characterEncoding,
			Map<String, Object> templateResolutionAttributes) {
		return new S3TemplateResource(amazonS3, bucketName, resourceName, toCharset(characterEncoding));
	}
	
	private Charset toCharset(String characterEncoding) {
		if (characterEncoding != null) {
			try {
				return Charset.forName(characterEncoding);
			} catch (UnsupportedCharsetException e) {
				log.error("Unexpected charset name: {}", characterEncoding);
				log.warn("Using default charset");
			}
		}
		return StandardCharsets.UTF_8;
	}
}
