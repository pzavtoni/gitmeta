package com.homework.task.api.controllers;

import com.homework.task.api.models.BranchDTO;
import com.homework.task.api.models.RepositoriesDTO;
import com.homework.task.api.models.RepositoryDTO;
import com.homework.task.services.RepositoryService;
import com.homework.task.services.models.ReposData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RestController
public class GitMetaController {

    private final RepositoryService gitReposService;

    @Autowired
    public GitMetaController(RepositoryService gitReposService) {
        this.gitReposService = gitReposService;
    }

    @GetMapping(value = "/repos", produces = "application/json")
    public Flux<ReposData> getReposMeta(@RequestParam String user) {
        //Flux<ReposData> account = gitReposService.getRepositories(user);
        return gitReposService.getRepositories(user);
        //return Mono.just(buildResponse(null));
    }

    private RepositoriesDTO buildResponse(ReposData repositoriesList) {
        return new RepositoriesDTO("petru", Collections.singletonList(
            new RepositoryDTO("repo1", Collections.singletonList(
                new BranchDTO("main", "sha1232134213")
            ))
        ));
    }
}
