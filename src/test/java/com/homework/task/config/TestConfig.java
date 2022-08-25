package com.homework.task.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class TestConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl("http://localhost:9001").build();
    }
}
