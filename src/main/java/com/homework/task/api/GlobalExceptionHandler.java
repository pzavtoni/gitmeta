package com.homework.task.api;

import com.homework.task.api.models.ErrorData;
import com.homework.task.ws.exceptions.GithubUserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
class GlobalExceptionHandler {

    @ExceptionHandler(GithubUserNotFoundException.class)
    ResponseEntity<ErrorData> userNotFound(GithubUserNotFoundException ex) {
        log.debug("handling exception::" + ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorData(ex.getStatus(), ex.getReason()));
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    ResponseEntity<ErrorData> mediaNotAcceptable(HttpMediaTypeNotAcceptableException ex) {
        log.debug("handling exception::" + ex);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ErrorData(HttpStatus.NOT_ACCEPTABLE.toString(), ex.getMessage()));
    }
}
