package com.homework.task.api.models;

import java.util.List;

public record RepositoryDTO(String name, String owner, List<BranchDTO> branches) {
}
