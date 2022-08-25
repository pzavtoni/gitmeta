package com.homework.task.services.impl;

import com.homework.task.services.models.ReposData;
import com.homework.task.ws.GitRepositoryService;
import com.homework.task.ws.dto.Commit;
import com.homework.task.ws.dto.GitBranchData;
import com.homework.task.ws.dto.GitRepositoryData;
import com.homework.task.ws.dto.Owner;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GitRepositoryServiceImplTest {

    public static final String USER_NAME = "user";
    public static final String FORK_1_NAME = "fork1";
    public static final String REPO_1_NAME = "repo1";
    public static final String FORK_2_NAME = "fork2";
    public static final String BRANCH_1 = "branch1";
    public static final String BRANCH_2 = "branch2";
    public static final String BRANCH_SHA_1 = "sha_1";
    public static final String BRANCH_SHA_2 = "sha_2";

    private final GitRepositoryService gitRepositoryService = mock(GitRepositoryService.class);
    private final GitRepositoryServiceImpl service = new GitRepositoryServiceImpl(gitRepositoryService);

    @Test
    public void getRepositories_Success() {
        Flux<GitRepositoryData> repos = Flux.fromIterable(mockReposList());
        Flux<GitBranchData> branches = Flux.fromIterable(mockBranchesList());

        when(gitRepositoryService.getUserRepositories(USER_NAME)).thenReturn(repos);
        when(gitRepositoryService.getBranchesByRepo(USER_NAME, REPO_1_NAME)).thenReturn(branches);

        Flux<ReposData> source = service.getRepositories(USER_NAME);

        StepVerifier.create(source)
            .expectNextMatches(repo ->
                repo.getName().equals(REPO_1_NAME) &&
                repo.getOwner().equals(USER_NAME) &&
                repo.getBranches().size() == 2 &&
                !repo.getFork()
            )
            .expectComplete()
            .verify();
    }

    private List<GitBranchData> mockBranchesList() {
        List<GitBranchData> branches = new ArrayList<>();

        branches.add(new GitBranchData(BRANCH_1, new Commit(BRANCH_SHA_1)));
        branches.add(new GitBranchData(BRANCH_2, new Commit(BRANCH_SHA_2)));

        return branches;
    }

    private List<GitRepositoryData> mockReposList() {
        List<GitRepositoryData> repos = new ArrayList<>();

        repos.add(new GitRepositoryData(FORK_1_NAME, new Owner(USER_NAME), true));
        repos.add(new GitRepositoryData(REPO_1_NAME, new Owner(USER_NAME), false));
        repos.add(new GitRepositoryData(FORK_2_NAME, new Owner(USER_NAME), true));

        return repos;
    }
}
