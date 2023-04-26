package com.ays.common.util.exception.handler;

import com.ays.AbstractRestControllerTest;
import com.ays.common.util.exception.model.AysError;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
class GlobalExceptionHandlerTest extends AbstractRestControllerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void givenInvalidArgumentToAnyEndpoint_whenThrowMethodArgumentTypeMismatchException_thenReturnAysError() {
        // Given
        MethodArgumentTypeMismatchException mockException = new MethodArgumentTypeMismatchException("test", String.class, "username", null, null);

        // When
        AysError mockAysError = AysError.subErrors(mockException)
                .header(AysError.Header.VALIDATION_ERROR.getName())
                .build();
        ResponseEntity<Object> mockResponseEntity = new ResponseEntity<>(mockAysError, HttpStatus.BAD_REQUEST);

        // Then
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleValidationErrors(mockException);

        Assertions.assertEquals(mockResponseEntity.getStatusCode(), responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getHeaders());
        Assertions.assertNotNull(responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());
        AysError aysError = (AysError) responseEntity.getBody();
        this.checkAysError(mockAysError, aysError);
    }

    private void checkAysError(AysError mockAysError, AysError aysError) {
        Assertions.assertNotNull(aysError.getRequestTime());
        Assertions.assertEquals(mockAysError.getHeader(), aysError.getHeader());
        Assertions.assertEquals(mockAysError.getMessage(), aysError.getMessage());
        Assertions.assertEquals(mockAysError.getIsSuccess(), aysError.getIsSuccess());
        Assertions.assertEquals(mockAysError.getSubErrors().size(), aysError.getSubErrors().size());
        Assertions.assertEquals(mockAysError.getSubErrors().get(0).getMessage(), aysError.getSubErrors().get(0).getMessage());
        Assertions.assertEquals(mockAysError.getSubErrors().get(0).getField(), aysError.getSubErrors().get(0).getField());
        Assertions.assertEquals(mockAysError.getSubErrors().get(0).getValue(), aysError.getSubErrors().get(0).getValue());
        Assertions.assertEquals(mockAysError.getSubErrors().get(0).getType(), aysError.getSubErrors().get(0).getType());
    }
}