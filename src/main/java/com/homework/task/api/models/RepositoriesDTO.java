package com.homework.task.api.models;

import java.util.List;

public record RepositoriesDTO(String ownerLogin, List<RepositoryDTO> repositories) {
}
