package com.homework.task.ws;

import com.homework.task.ws.dto.GitBranchData;
import com.homework.task.ws.dto.GitRepositoryData;
import com.homework.task.ws.exceptions.GithubUserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class GitRepositoryService {
    public static final String USER_NOT_FOUND_MESSAGE = "User does not exist!";
    public static final String USERS_PATH = "/users";
    public static final String REPOSITORIES_PATH = "/repos";

    private final WebClient webClient;

    @Autowired
    public GitRepositoryService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<GitRepositoryData> getUserRepositories(String user) {
        return webClient.get()
            .uri(String.format("%s/%s/repos", USERS_PATH, user))
            .retrieve()
            .onStatus(status -> status == HttpStatus.NOT_FOUND, this::handleErrors)
            .bodyToFlux(GitRepositoryData.class);
    }

    public Flux<GitBranchData> getBranchesByRepo(String user, String repo) {
        return webClient.get()
            .uri(String.format("%s/%s/%s/branches", REPOSITORIES_PATH, user, repo))
            .retrieve()
            .bodyToFlux(GitBranchData.class);
    }

    private Mono<Throwable> handleErrors(ClientResponse response ){
        return response.bodyToMono(String.class).flatMap(body -> {
            log.debug(String.format("[WS - User Not Found] Body: %s", body));
            return Mono.error(new GithubUserNotFoundException(response.statusCode().toString(), USER_NOT_FOUND_MESSAGE));
        });
    }
}
