package com.homework.task.services;

import com.homework.task.services.models.ReposData;

public interface RepositoryService {
    ReposData getRepositories(String user);
}
