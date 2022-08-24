package com.homework.task.services.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReposData {
    String name;
    String owner;
    Boolean fork;
    List<Branch> branches;
}
