package com.homework.task.ws.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GitBranchData {

    String name;

    @JsonProperty("commit")
    Commit commitData;
}
