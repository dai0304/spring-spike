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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Locale;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.util.LoggingUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;

/**
 * {@link ITemplateResource} implementation for Amazon S3 template.
 */
@Slf4j
@RequiredArgsConstructor
public class S3TemplateResource implements ITemplateResource {
	
	private static String computeBaseName(String path) {
		
		if (path == null || path.length() == 0) {
			return null;
		}
		
		// First remove a trailing '/' if it exists
		final String basePath = (path.charAt(path.length() - 1) == '/' ? path.substring(0, path.length() - 1) : path);
		
		final int slashPos = basePath.lastIndexOf('/');
		if (slashPos != -1) {
			final int dotPos = basePath.lastIndexOf('.');
			if (dotPos != -1 && dotPos > slashPos + 1) {
				return basePath.substring(slashPos + 1, dotPos);
			}
			return basePath.substring(slashPos + 1);
		} else {
			final int dotPos = basePath.lastIndexOf('.');
			if (dotPos != -1) {
				return basePath.substring(0, dotPos);
			}
		}
		
		return (basePath.length() > 0 ? basePath : null);
		
	}
	
	
	@NonNull
	private final AmazonS3 amazonS3;
	
	@NonNull
	private final String bucketName;
	
	@NonNull
	private final String objectName;
	
	@NonNull
	private final Charset charset;
	
	@Override
	public String getDescription() {
		return "Amazon s3 resource [bucket='" + this.bucketName + "' and object='" + this.objectName + "']";
	}
	
	@Override
	public String getBaseName() {
		return computeBaseName(objectName);
	}
	
	@Override
	public boolean exists() {
		try {
			return amazonS3.doesObjectExist(bucketName, objectName);
		} catch (AmazonClientException e) {
			log.error("Failed to access template on S3: {}", getDescription());
		}
		return false;
	}
	
	@Override
	public Reader reader() throws IOException {
		try {
			S3Object object = amazonS3.getObject(bucketName, objectName);
			return new InputStreamReader(object.getObjectContent(), charset);
		} catch (AmazonClientException e) {
			log.error("Failed to load template: {}", getDescription());
			throw new IOException(e);
		}
	}
	
	@Override
	public ITemplateResource relative(String relativeLocation) {
		String relativeKey = this.objectName + "/" + relativeLocation;
		return new S3TemplateResource(amazonS3, this.bucketName, relativeKey, charset);
	}
}
