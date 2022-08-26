package com.homework.task.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ExternalApiConfig {

    @Value("${github.authentication.token}")
    private String githubAccessToken;

    @Value("${github.baseUrl}")
    private String baseURL;

    public static final String APPLICATION_GITHUB_JSON = "application/vnd.github+json";
    public static final String BEARER_TYPE = "Bearer";

    @Bean
    public WebClient webClient() {
        WebClient.Builder webClient = WebClient.builder().baseUrl(baseURL);

        if(githubAccessToken != null && !githubAccessToken.isBlank()) {
            webClient.defaultHeader(HttpHeaders.AUTHORIZATION, String.format("%s %s", BEARER_TYPE, githubAccessToken));
        }

        return webClient
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, APPLICATION_GITHUB_JSON)
            .build();
    }
}
