package com.homework.task.ws.exceptions;

import lombok.Getter;

@Getter
public class GithubUserNotFoundException extends RuntimeException {

    final String status;
    final String reason;

    public GithubUserNotFoundException(String status, String reason) {
        super(reason);

        this.reason = reason;
        this.status = status;
    }
}
