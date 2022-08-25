package com.homework.task.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.task.api.controllers.GitMetaController;
import com.homework.task.api.models.BranchDTO;
import com.homework.task.api.models.RepositoryDTO;
import com.homework.task.config.TestConfig;
import com.homework.task.services.impl.GitRepositoryServiceImpl;
import com.homework.task.ws.GitRepositoryService;
import com.homework.task.ws.dto.Commit;
import com.homework.task.ws.dto.GitBranchData;
import com.homework.task.ws.dto.GitRepositoryData;
import com.homework.task.ws.dto.Owner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = GitMetaController.class)
@Import({TestConfig.class, GitRepositoryServiceImpl.class, GitRepositoryService.class})
public class GitMetaControllerTest {

    public static final String USER_NAME = "user";
    public static final String REPO_NAME = "repo1";
    public static final String REPO_NAME_2 = "repo2";
    public static final String BRANCH_NAME = "branch1";
    public static final String LAST_COMMIT_HASH = "sha1";

    private GitRepositoryService gitRepositoryService;

    private GitRepositoryServiceImpl service;

    private ClientAndServer mockServer;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setupMockServer() {
        mockServer = ClientAndServer.startClientAndServer(9001);
        gitRepositoryService = new GitRepositoryService(WebClient.builder()
            .baseUrl("http://localhost:" + mockServer.getLocalPort()).build());
        service = new GitRepositoryServiceImpl(gitRepositoryService);
    }

    @AfterEach
    public void tearDownServer() {
        mockServer.stop();
    }

    @Test
    void getUsersRepos() throws JsonProcessingException {
        List<GitRepositoryData> repos = new ArrayList<>();
        repos.add(new GitRepositoryData(REPO_NAME, new Owner(USER_NAME), false));
        repos.add(new GitRepositoryData(REPO_NAME_2, new Owner(USER_NAME), true));

        List<GitBranchData> branches = new ArrayList<>();
        branches.add(new GitBranchData(BRANCH_NAME, new Commit(LAST_COMMIT_HASH)));

        mockGitApiCall(String.format("/users/%s/repos", USER_NAME), mapper.writeValueAsString(repos));
        mockGitApiCall(String.format("/repos/%s/%s/branches", USER_NAME, REPO_NAME), mapper.writeValueAsString(branches));

        List<RepositoryDTO> result = webTestClient.get()
            .uri(String.format("/repos?user=%s", USER_NAME))
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(RepositoryDTO.class)
            .returnResult()
            .getResponseBody();

        assertEquals(1, result.size());
        RepositoryDTO repo = result.get(0);

        assertEquals(USER_NAME, repo.owner());
        assertEquals(REPO_NAME, repo.name());

        assertEquals(1, result.get(0).branches().size());
        BranchDTO branch = result.get(0).branches().get(0);

        assertEquals(BRANCH_NAME, branch.name());
        assertEquals(LAST_COMMIT_HASH, branch.lastCommitSHA());
    }

    @Test
    void notAcceptableContentType() {
        webTestClient.get()
            .uri(String.format("/repos?user=%s", USER_NAME))
            .header(HttpHeaders.ACCEPT, "application/xml")
            .exchange().expectStatus().isEqualTo(HttpStatus.NOT_ACCEPTABLE);
    }

    private void mockGitApiCall(String path, String body) {
        mockServer.when(
            request()
                .withMethod(HttpMethod.GET.name())
                .withPath(path)
        ).respond(
            response()
                .withStatusCode(HttpStatus.OK.value())
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(body)
        );
    }
}
