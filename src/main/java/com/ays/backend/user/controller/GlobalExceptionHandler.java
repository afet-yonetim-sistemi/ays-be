package com.ays.backend.user.controller;

import com.ays.backend.user.controller.payload.response.MessageResponse;
import com.ays.backend.user.exception.DeviceNotFoundException;
import com.ays.backend.user.exception.RoleNotFoundException;
import com.ays.backend.user.exception.UserAlreadyExistsException;
import com.ays.backend.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler acting as controller advice for certain use cases happened in the controller.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<MessageResponse> handleUserAlreadyExists(
            UserAlreadyExistsException ex) {
        log.warn("Username already exists for the request.");
        MessageResponse messageResponse = new MessageResponse(ex.getMessage());
        return ResponseEntity.badRequest().body(messageResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<MessageResponse> handleUserNotFound(
            UserNotFoundException ex) {
        log.warn("User not found for the request.");
        MessageResponse messageResponse = new MessageResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageResponse);
    }

    @ExceptionHandler(DeviceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<MessageResponse> handleDeviceNotFound(
            DeviceNotFoundException ex) {
        log.warn("Device not found for the request.");
        MessageResponse messageResponse = new MessageResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageResponse);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<MessageResponse> handleRoleNotFound(
            RoleNotFoundException ex) {
        log.warn("UserType not found for the request.");
        MessageResponse messageResponse = new MessageResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageResponse);
    }

}
