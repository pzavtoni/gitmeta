package com.homework.task.api.controllers;

import com.homework.task.api.models.BranchDTO;
import com.homework.task.api.models.RepositoryDTO;
import com.homework.task.services.RepositoryService;
import com.homework.task.services.models.ReposData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class GitMetaController {

    private final RepositoryService gitReposService;

    @Autowired
    public GitMetaController(RepositoryService gitReposService) {
        this.gitReposService = gitReposService;
    }

    @GetMapping(value = "/repos", produces = "application/json")
    public Flux<RepositoryDTO> getReposMeta(@RequestParam String user) {

        return gitReposService.getRepositories(user)
            .map(repo -> new RepositoryDTO(
                repo.getName(),
                repo.getOwner(),
                getBranches(repo)
            ));
    }

    private List<BranchDTO> getBranches(ReposData repo) {
        return repo.getBranches()
            .stream()
            .map(branch -> new BranchDTO(branch.getName(), branch.getLastCommit()))
            .toList();
    }
}
