package com.homework.task.services.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Branch {
    String name;
    String lastCommit;
}
