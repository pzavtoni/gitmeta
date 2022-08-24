package com.homework.task.ws.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitBranchData(String name, @JsonProperty("commit") Commit commitData) {
}
