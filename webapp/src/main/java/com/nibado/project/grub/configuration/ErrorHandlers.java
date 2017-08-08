package com.nibado.project.grub.configuration;

import com.nibado.project.grub.aspect.AccessException;
import com.nibado.project.grub.users.service.exception.AuthenticationException;
import com.nibado.project.grub.users.service.exception.UserAlreadyExistsException;
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

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({AccessException.class})
    @ResponseBody
    public ErrorResponse handle(AccessException e) {
        log.error("AccessException: {}", e.getMessage(), e);
        return new ErrorResponse("NO_ACCESS", e.getMessage() );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseBody
    public ErrorResponse handle(IllegalArgumentException e) {
        log.error("IllegalArgumentException: {}", e.getMessage(), e);
        return new ErrorResponse("ILLEGAL_ARGUMENT", e.getMessage() );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UserAlreadyExistsException.class})
    @ResponseBody
    public ErrorResponse handle(UserAlreadyExistsException e) {
        log.error("UserAlreadyExistsException: {}", e.getMessage(), e);
        return new ErrorResponse("ALREADY_EXISTS", e.getMessage() );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Throwable.class})
    @ResponseBody
    public ErrorResponse handleThrowableSafetyNet(Throwable ex) {
        log.error("Unknown error: {}", ex.getMessage(), ex);
        return new ErrorResponse("UNKNOWN", "An unknown error occurred");
    }

    @Value
    public static class ErrorResponse {
        private final String code;
        private final String message;
    }
}
