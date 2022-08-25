package com.homework.task.ws;

import com.homework.task.ws.dto.Commit;
import com.homework.task.ws.dto.GitBranchData;
import com.homework.task.ws.dto.GitRepositoryData;
import com.homework.task.ws.dto.Owner;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.hamcrest.Matchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GitRepositoryServiceTest {

    public static final String USER_NAME = "unidentified";
    public static final boolean FORK_FLAG = false;
    public static final String REPO_NAME = "Repo1";
    public static final String BRANCH_NAME = "brunch";
    public static final String SHA_COMMIT = "shaCommit";

    WebClient webClient = mock(WebClient.class);

    GitRepositoryService webService = new GitRepositoryService(webClient);

    @Test
    public void getUserRepositories_Success() {
        GitRepositoryData repo = new GitRepositoryData(
            REPO_NAME,
            new Owner(USER_NAME),
            FORK_FLAG
        );

        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(GitRepositoryData.class)).thenReturn(Flux.fromIterable(Collections.singletonList(repo)));

        Flux<GitRepositoryData> source = webService.getUserRepositories(USER_NAME);

        StepVerifier.create(source)
            .expectNextMatches(item ->
                item.fork() == FORK_FLAG && item.name().equals(REPO_NAME) && item.owner().login().equals(USER_NAME)
            )
            .expectComplete()
            .verify();
    }

    @Test
    public void getBranchesByRepo_Success() {
        GitBranchData branch = new GitBranchData(
            BRANCH_NAME,
            new Commit(SHA_COMMIT)
        );

        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(GitBranchData.class)).thenReturn(Flux.fromIterable(Collections.singletonList(branch)));

        Flux<GitBranchData> source = webService.getBranchesByRepo(USER_NAME, REPO_NAME);

        StepVerifier.create(source)
            .expectNextMatches(item ->
                item.name().equals(BRANCH_NAME) && item.commitData().lastCommitHash().equals(SHA_COMMIT)
            )
            .expectComplete()
            .verify();
    }
}
