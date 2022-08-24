package com.homework.task.ws.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Commit(@JsonProperty("sha") String lastCommitHash) {
}
