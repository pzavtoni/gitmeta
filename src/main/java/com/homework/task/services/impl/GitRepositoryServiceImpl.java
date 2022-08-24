package com.homework.task.services.impl;

import com.homework.task.services.RepositoryService;
import com.homework.task.services.models.Branch;
import com.homework.task.services.models.ReposData;
import com.homework.task.ws.GitRepositoryService;
import com.homework.task.ws.dto.GitRepositoryData;
import lombok.val;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.function.Function;

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
            .filter(repo -> !repo.fork())
            .flatMap(retrieveBranchesByRepo(user));
    }

    private Function<GitRepositoryData, Publisher<? extends ReposData>> retrieveBranchesByRepo(String user) {
        return notForkRepo ->
            Mono.zip(
                Mono.just(notForkRepo),
                gitRepositoryService.getBranchesByRepo(user, notForkRepo.name())
                    .map(branch -> new Branch(branch.name(), branch.commitData().lastCommitHash())).collectList()
            ).map(setReposBranches());
    }

    private Function<Tuple2<GitRepositoryData, List<Branch>>, ReposData> setReposBranches() {
        return zipTuple -> {
            val repo = zipTuple.getT1();
            val branches = zipTuple.getT2();
            return new ReposData(repo.name(), repo.owner().login(), repo.fork(), branches);
        };
    }
}
