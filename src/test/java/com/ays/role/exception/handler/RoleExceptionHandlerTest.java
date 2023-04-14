package com.ays.role.exception.handler;

import com.ays.BaseServiceTest;
import com.ays.role.exception.AysRoleNotExistException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class RoleExceptionHandlerTest extends BaseServiceTest {

    @InjectMocks
    private RoleExceptionHandler roleExceptionHandler;

    @Test
    void shouldHandleRoleNotFoundException() {
        // given
        AysRoleNotExistException ex = new AysRoleNotExistException("UserType Not Found!");
        ResponseEntity<MessageResponse> responseEntity = roleExceptionHandler.handleRoleNotFound(ex);

        // when && then
        MessageResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("UserType Not Found!", responseBody.getMessage());
    }
}