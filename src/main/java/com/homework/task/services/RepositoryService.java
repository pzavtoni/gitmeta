package com.homework.task.services;

import com.homework.task.services.models.ReposData;
import reactor.core.publisher.Flux;

public interface RepositoryService {
    Flux<ReposData> getRepositories(String user);
}
