package com.homework.task.services.models;

import lombok.Data;

import java.util.List;

@Data
public class ReposData {
    String name;
    String login;
    List<Branch> branches;
}
