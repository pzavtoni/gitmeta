package com.homework.task.api.models;

import java.util.List;

public record RepositoryDTO(String name, List<BranchDTO> branches) {
}
