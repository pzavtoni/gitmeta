package com.homework.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.task.api.controllers.GitMetaController;
import com.homework.task.api.models.RepositoryDTO;
import com.homework.task.services.impl.GitRepositoryServiceImpl;
import com.homework.task.ws.GitRepositoryService;
import com.homework.task.ws.dto.Commit;
import com.homework.task.ws.dto.GitBranchData;
import com.homework.task.ws.dto.GitRepositoryData;
import com.homework.task.ws.dto.Owner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class HomeworkApplicationTest {

    public static final String USER_NAME = "user";
    public static final String REPO_NAME = "repo1";
    public static final String REPO_NAME_2 = "repo2";
    public static final String BRANCH_NAME = "branch1";
    public static final String LAST_COMMIT_HASH = "sha1";
    private GitMetaController controller;

    private GitRepositoryService gitRepositoryService;

    private GitRepositoryServiceImpl service;

    private ClientAndServer mockServer;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setupMockServer() {
        mockServer = ClientAndServer.startClientAndServer(9001);
        gitRepositoryService = new GitRepositoryService(WebClient.builder()
            .baseUrl("http://localhost:" + mockServer.getLocalPort()).build());
        service = new GitRepositoryServiceImpl(gitRepositoryService);
        controller = new GitMetaController(service);
    }

    @AfterEach
    public void tearDownServer() {
        mockServer.stop();
    }

    @Test
    public void retrieveReposByUser() throws JsonProcessingException {
        List<GitRepositoryData> repos = new ArrayList<>();
        repos.add(new GitRepositoryData(REPO_NAME, new Owner(USER_NAME), false));
        repos.add(new GitRepositoryData(REPO_NAME_2, new Owner(USER_NAME), true));

        List<GitBranchData> branches = new ArrayList<>();
        branches.add(new GitBranchData(BRANCH_NAME, new Commit(LAST_COMMIT_HASH)));

        mockServer.when(
            request()
                .withMethod(HttpMethod.GET.name())
                .withPath(String.format("/users/%s/repos", USER_NAME))
        ).respond(
            response()
                .withStatusCode(HttpStatus.OK.value())
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(mapper.writeValueAsString(repos))
        );

        mockServer.when(
            request()
                .withMethod(HttpMethod.GET.name())
                .withPath(String.format("/repos/%s/%s/branches", USER_NAME, REPO_NAME))
        ).respond(
            response()
                .withStatusCode(HttpStatus.OK.value())
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(mapper.writeValueAsString(branches))
        );

        Flux<RepositoryDTO> resp = controller.getReposMeta(USER_NAME);
        StepVerifier.create(resp)
            .expectNextMatches(repo ->
                repo.name().equals(REPO_NAME) &&
                    repo.owner().equals(USER_NAME) &&
                    repo.branches().size() == 1 &&
                    repo.branches().get(0).name().equals(BRANCH_NAME) &&
                    repo.branches().get(0).lastCommitSHA().equals(LAST_COMMIT_HASH)
            )
            .expectComplete()
            .verify();
    }

    @Test
    public void retrieveReposByUser_NotFound() {
        List<GitRepositoryData> repos = new ArrayList<>();
        repos.add(new GitRepositoryData(REPO_NAME, new Owner(USER_NAME), false));
        repos.add(new GitRepositoryData(REPO_NAME_2, new Owner(USER_NAME), true));

        List<GitBranchData> branches = new ArrayList<>();
        branches.add(new GitBranchData(BRANCH_NAME, new Commit(LAST_COMMIT_HASH)));

        mockServer.when(
            request()
                .withMethod(HttpMethod.GET.name())
                .withPath(String.format("/users/%s/repos", USER_NAME))
        ).respond(
            response()
                .withStatusCode(HttpStatus.NOT_FOUND.value())
        );

        Flux<RepositoryDTO> resp = controller.getReposMeta(USER_NAME);
        StepVerifier.create(resp)
            .expectComplete()
            .verify();
    }
}
