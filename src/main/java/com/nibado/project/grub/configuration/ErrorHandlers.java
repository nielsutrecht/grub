package com.nibado.project.grub.configuration;

import com.nibado.project.grub.service.exception.AuthenticationException;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ErrorHandlers {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({AuthenticationException.class})
    @ResponseBody
    public ErrorResponse handle(AuthenticationException e) {
        log.error("AuthenticationException: {}", e.getMessage(), e);
        return new ErrorResponse("UNAUTHORIZED", e.getMessage() );
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Throwable.class})
    @ResponseBody
    public ErrorResponse handleThrowableSafetyNet(Throwable ex) {
        log.error("Unknown error: {}", ex.getMessage(), ex);
        return new ErrorResponse("UNKNOWN", "An unknown error occured");
    }

    @Value
    public static class ErrorResponse {
        private final String code;
        private final String message;
    }
}
