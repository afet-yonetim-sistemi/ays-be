package com.ays.user.exception.handler;

import com.ays.BaseServiceTest;
import com.ays.user.util.exception.AysUserNotExistByUsernameException;
import com.ays.user.util.exception.handler.UserExceptionHandler;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserExceptionHandlerTest extends BaseServiceTest {

    @InjectMocks
    private UserExceptionHandler userExceptionHandler;

    @Test
    void shouldHandleUserAlreadyExistsException() {
        // given
        AysUserAlreadyExistsException ex = new AysUserAlreadyExistsException("Something went wrong!");
        ResponseEntity<MessageResponse> responseEntity = userExceptionHandler.handleUserAlreadyExists(ex);

        // when && then
        MessageResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Something went wrong!", responseBody.getMessage());
    }

    @Test
    void shouldHandleUserNotFoundException() {
        // given
        AysUserNotExistByUsernameException ex = new AysUserNotExistByUsernameException("User Not Found!");
        ResponseEntity<MessageResponse> responseEntity = userExceptionHandler.handleUserNotFound(ex);

        // when && then
        MessageResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("User Not Found!", responseBody.getMessage());
    }

}