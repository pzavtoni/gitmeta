package com.homework.task.ws;

import com.homework.task.services.models.Branch;
import com.homework.task.ws.dto.GitBranchData;
import com.homework.task.ws.dto.GitRepositoryData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class GitRepositoryService {
    private final WebClient webClient;

    @Autowired
    public GitRepositoryService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<GitRepositoryData> getUserRepositories(String user) {
        return webClient.get()
            .uri(String.format("/users/%s/repos", user))
            .retrieve()
            .bodyToFlux(GitRepositoryData.class);
    }

    public Flux<GitBranchData> getBranchesByRepo(String user, String repo) {
        return webClient.get()
            .uri(String.format("/repos/%s/%s/branches", user, repo))
            .retrieve()
            .bodyToFlux(GitBranchData.class);
    }
}
