package com.homework.task.ws.dto;

import lombok.Data;

@Data
public class GitRepositoryData {
    String name;
    Owner owner;
    Boolean fork;
}
