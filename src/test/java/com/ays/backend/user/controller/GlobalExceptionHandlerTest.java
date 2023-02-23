package com.ays.backend.user.controller;

import com.ays.backend.base.BaseServiceTest;
import com.ays.backend.user.controller.payload.response.MessageResponse;
import com.ays.backend.user.exception.DeviceNotFoundException;
import com.ays.backend.user.exception.RoleNotFoundException;
import com.ays.backend.user.exception.UserAlreadyExistsException;
import com.ays.backend.user.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class GlobalExceptionHandlerTest extends BaseServiceTest {
    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void shouldHandleUserAlreadyExistsException() {
        // given
        UserAlreadyExistsException ex = new UserAlreadyExistsException("Something went wrong!");
        ResponseEntity<MessageResponse> responseEntity = globalExceptionHandler.handleUserAlreadyExists(ex);

        // when && then
        MessageResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Something went wrong!", responseBody.getMessage());
    }

    @Test
    void shouldHandleUserNotFoundExistsException() {
        // given
        UserNotFoundException ex = new UserNotFoundException("User Not Found!");
        ResponseEntity<MessageResponse> responseEntity = globalExceptionHandler.handleUserNotFound(ex);

        // when && then
        MessageResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("User Not Found!", responseBody.getMessage());
    }

    @Test
    void shouldHandleRoleNotFoundExistsException() {
        // given
        RoleNotFoundException ex = new RoleNotFoundException("Role Not Found!");
        ResponseEntity<MessageResponse> responseEntity = globalExceptionHandler.handleRoleNotFound(ex);

        // when && then
        MessageResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Role Not Found!", responseBody.getMessage());
    }

    @Test
    void shouldHandleDeviceNotFoundExistsException() {
        // given
        DeviceNotFoundException ex = new DeviceNotFoundException("Device Not Found!");
        ResponseEntity<MessageResponse> responseEntity = globalExceptionHandler.handleDeviceNotFound(ex);

        // when && then
        MessageResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Device Not Found!", responseBody.getMessage());
    }
}
