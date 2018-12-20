/*
 * Copyright 2018 the original author or authors.
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

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import jp.xet.spike.springspike.model.Foobar;
import jp.xet.spike.springspike.model.FoobarRepository;

/**
 * Spike application.
 *
 * @author miyamoto.daisuke
 * @since #version#
 */
@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class SpringSpikeApplication implements CommandLineRunner {
	
	private final FoobarRepository repo;
	
	
	public static void main(String[] args) {
		SpringApplication.run(SpringSpikeApplication.class, args);
	}
	
	@Override
	@Transactional
	public void run(String... args) throws Exception {
		repo.create(Foobar.builder()
			.foobarCode(UUID.randomUUID().toString())
			.foobarName("name1")
			.build());
	}
}
