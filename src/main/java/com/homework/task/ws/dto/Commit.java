package com.homework.task.ws.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Commit {

    @JsonProperty("sha")
    String lastCommitHash;
}
