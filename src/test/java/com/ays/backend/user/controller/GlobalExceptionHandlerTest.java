package com.ays.backend.user.controller;

import com.ays.backend.base.BaseServiceTest;
import com.ays.backend.user.controller.payload.response.MessageResponse;
import com.ays.backend.user.exception.UserAlreadyExistsException;
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
}
