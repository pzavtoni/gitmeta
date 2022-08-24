package com.homework.task.services.impl;

import com.homework.task.services.RepositoryService;
import com.homework.task.services.models.Branch;
import com.homework.task.services.models.ReposData;
import com.homework.task.ws.GitRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
public class GitRepositoryServiceImpl implements RepositoryService {

    private final GitRepositoryService gitRepositoryService;

    @Autowired
    public GitRepositoryServiceImpl(GitRepositoryService gitRepositoryService) {
        this.gitRepositoryService = gitRepositoryService;
    }

    @Override
    public Flux<ReposData> getRepositories(String user) {
        return gitRepositoryService.getUserRepositories(user)
            .filter(repo -> !repo.getFork())
            .flatMap(notForkRepo ->
                Mono.zip(
                    Mono.just(notForkRepo),
                    gitRepositoryService.getBranchesByRepo(user, notForkRepo.getName())
                        .map(branch -> new Branch(branch.getName(), branch.getCommitData().getLastCommitHash())).collectList())
                    .map(a -> new ReposData(
                        a.getT1().getName(),
                        a.getT1().getOwner().getLogin(),
                        a.getT2()
                    )
                )
            );
    }
}
