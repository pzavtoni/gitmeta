package com.homework.task.api.controllers;

import com.homework.task.api.models.RepositoriesDTO;
import com.homework.task.services.RepositoryService;
import com.homework.task.services.models.ReposData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitMetaController {

    private final RepositoryService gitReposService;

    @Autowired
    public GitMetaController(RepositoryService gitReposService) {
        this.gitReposService = gitReposService;
    }

    @GetMapping(value = "/repos", produces = "application/json")
    public RepositoriesDTO getReposMeta(@RequestParam String user) {
        ReposData account = gitReposService.getRepositories(user);

        return buildResponse(account);
    }

    private RepositoriesDTO buildResponse(ReposData repositoriesList) {
        return RepositoriesDTO.builder()
            .build();
    }
}
