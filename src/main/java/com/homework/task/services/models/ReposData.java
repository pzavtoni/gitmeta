package com.homework.task.services.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Data
@AllArgsConstructor
public class ReposData {
    String name;
    String owner;
    List<Branch> branches;
}
