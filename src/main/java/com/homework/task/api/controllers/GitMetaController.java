package com.homework.task.api.controllers;

import com.homework.task.api.models.BranchDTO;
import com.homework.task.api.models.RepositoryDTO;
import com.homework.task.services.RepositoryService;
import com.homework.task.services.models.ReposData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@Api(value = "GitMetadataController")
@RestController
public class GitMetaController {

    private final RepositoryService gitReposService;

    @Autowired
    public GitMetaController(RepositoryService gitReposService) {
        this.gitReposService = gitReposService;
    }

    @ApiOperation(
        value = "Get list of Users's GitHub repositories",
        response = RepositoryDTO.class,
        responseContainer = "List",
        tags = "getUsersRepos"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 406, message = "Not Accepted") })
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
