package com.ays.common.util.exception.handler;

import com.ays.AbstractRestControllerTest;
import com.ays.common.util.exception.AysAuthException;
import com.ays.common.util.exception.AysNotExistException;
import com.ays.common.util.exception.AysProcessException;
import com.ays.common.util.exception.model.AysError;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.lang.reflect.Method;
import java.sql.SQLException;

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

    @Test
    void givenInvalidArgumentToAnyEndpoint_whenThrowMethodArgumentNotValidException_thenReturnAysError() throws NoSuchMethodException {

        // Given
        Method method = GlobalExceptionHandlerTest.class.getDeclaredMethod("givenInvalidArgumentToAnyEndpoint_whenThrowMethodArgumentNotValidException_thenReturnAysError");
        int parameterIndex = -1;

        MethodParameter mockParameter = new MethodParameter(method, parameterIndex);
        BindingResult mockBindingResult = new BeanPropertyBindingResult(null, null);
        MethodArgumentNotValidException mockException = new MethodArgumentNotValidException(mockParameter, mockBindingResult);

        // When
        AysError mockAysError = AysError.subErrors(mockException.getBindingResult().getFieldErrors())
                .header(AysError.Header.VALIDATION_ERROR.getName())
                .build();
        ResponseEntity<Object> mockResponseEntity = new ResponseEntity<>(mockAysError, HttpStatus.BAD_REQUEST);

        // Then
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleValidationErrors(mockException);

        Assertions.assertEquals(mockResponseEntity.getStatusCode(), responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getHeaders());
        Assertions.assertNotNull(responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());

    }

    @Test
    void givenInvalidArgumentToAnyEndpoint_whenThrowConstraintViolationException_thenReturnAysError() {

        // Given
        ConstraintViolationException mockException = new ConstraintViolationException(null);

        // When
        AysError mockAysError = AysError.subErrors(mockException.getConstraintViolations())
                .header(AysError.Header.VALIDATION_ERROR.getName())
                .build();
        ResponseEntity<Object> mockResponseEntity = new ResponseEntity<>(mockAysError, HttpStatus.BAD_REQUEST);

        // Then
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handlePathVariableErrors(mockException);

        Assertions.assertEquals(mockResponseEntity.getStatusCode(), responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getHeaders());
        Assertions.assertNotNull(responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());

    }

    @Test
    void givenResourceNotFound_whenThrowAysNotExistException_thenReturnAysErrorWithReturnAysError() {

        // Given
        AysNotExistException mockException = new AysNotExistException("Resource not found") {
            private static final long serialVersionUID = 1L;

            @Override
            public String getMessage() {
                return "Resource not found";
            }
        };

        // When
        AysError mockAysError = AysError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .header(AysError.Header.NOT_FOUND.getName())
                .message(mockException.getMessage())
                .build();
        ResponseEntity<Object> mockResponseEntity = new ResponseEntity<>(mockAysError, HttpStatus.NOT_FOUND);

        // Then
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleNotExistError(mockException);
        Assertions.assertEquals(mockResponseEntity.getStatusCode(), responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getHeaders());
        Assertions.assertNotNull(responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());

    }

    @Test
    void givenInternalServerException_whenThrowAysProcessException_WithReturnAysError() {

        // Given
        AysProcessException mockException = new AysProcessException("Internal server error") {
            private static final long serialVersionUID = 1L;

            @Override
            public String getMessage() {
                return "Internal server error";
            }
        };

        // When
        AysError mockAysError = AysError.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(AysError.Header.PROCESS_ERROR.getName())
                .message(mockException.getMessage())
                .build();
        ResponseEntity<Object> mockResponseEntity = new ResponseEntity<>(mockAysError, HttpStatus.INTERNAL_SERVER_ERROR);

        // Then
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleProcessError(mockException);
        Assertions.assertEquals(mockResponseEntity.getStatusCode(), responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getHeaders());
        Assertions.assertNotNull(responseEntity.getStatusCode());

    }

    @Test
    void givenAccessDeniedException_whenThrowAccessDeniedException_thenReturnAysError() {

        // Given
        AccessDeniedException mockException = new AccessDeniedException("Access denied");

        // When
        AysError mockAysError = AysError.builder()
                .httpStatus(HttpStatus.FORBIDDEN)
                .header(AysError.Header.AUTH_ERROR.getName())
                .build();
        ResponseEntity<Object> mockResponseEntity = new ResponseEntity<>(mockAysError, HttpStatus.FORBIDDEN);

        // Then
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleAccessDeniedError(mockException);
        Assertions.assertEquals(mockResponseEntity.getStatusCode(), responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getHeaders());
        Assertions.assertNotNull(responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());

    }

    @Test
    void givenSQLException_whenThrowSQLException_thenReturnAysError() {

        // Given
        SQLException mockException = new SQLException("Database error");

        // When
        AysError mockAysError = AysError.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(AysError.Header.DATABASE_ERROR.getName())
                .build();
        ResponseEntity<Object> mockResponseEntity = new ResponseEntity<>(mockAysError, HttpStatus.INTERNAL_SERVER_ERROR);

        // Then
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleSQLError(mockException);
        Assertions.assertEquals(mockResponseEntity.getStatusCode(), responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getHeaders());
        Assertions.assertNotNull(responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());

    }

    @Test
    void givenAuthenticationFailure_whenThrowAysAuthException_thenReturnAysError() {
        // Given
        AysAuthException mockException = new AysAuthException("Authentication failed") {
            private static final long serialVersionUID = 1L;

            @Override
            public String getMessage() {
                return "Authentication failed";
            }
        };

        // When
        AysError mockAysError = AysError.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .header(AysError.Header.AUTH_ERROR.getName())
                .message(mockException.getMessage())
                .build();
        ResponseEntity<Object> mockResponseEntity = new ResponseEntity<>(mockAysError, HttpStatus.UNAUTHORIZED);

        // Then
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleAuthError(mockException);
        Assertions.assertEquals(mockResponseEntity.getStatusCode(), responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getHeaders());
        Assertions.assertNotNull(responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());

    }

    @Test
    void givenUnsupportedHttpMethod_whenThrowHttpRequestMethodNotSupportedException_thenReturnAysError() {

        // Given
        HttpRequestMethodNotSupportedException mockException = new HttpRequestMethodNotSupportedException("Unsupported method");

        // When
        AysError mockAysError = AysError.builder()
                .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                .header(AysError.Header.VALIDATION_ERROR.getName())
                .build();
        ResponseEntity<Object> mockResponseEntity = new ResponseEntity<>(mockAysError, HttpStatus.METHOD_NOT_ALLOWED);

        // Then
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleHttpRequestMethodNotSupportedException(mockException);
        Assertions.assertEquals(mockResponseEntity.getStatusCode(), responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getHeaders());
        Assertions.assertNotNull(responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());

    }

    @Test
    void givenUnsupportedMediaType_whenThrowHttpMediaTypeNotSupportedException_thenReturnAysError() {

        // Given
        HttpMediaTypeNotSupportedException mockException = new HttpMediaTypeNotSupportedException("Unsupported media type");

        // When
        AysError mockAysError = AysError.builder()
                .httpStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .header(AysError.Header.VALIDATION_ERROR.getName())
                .build();
        ResponseEntity<Object> mockResponseEntity = new ResponseEntity<>(mockAysError, HttpStatus.UNSUPPORTED_MEDIA_TYPE);

        // Then
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleHttpMediaTypeNotSupportedException(mockException);
        Assertions.assertEquals(mockResponseEntity.getStatusCode(), responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getHeaders());
        Assertions.assertNotNull(responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());
    }

    @Test
    void givenDataAccessException_whenThrowDataAccessException_thenReturnAysErrorWithInternalServerErrorStatus() {

        // Given
        DataAccessException mockException = new DataAccessException("Data access error") {
            private static final long serialVersionUID = 1L;
        };

        // When
        AysError mockAysError = AysError.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(AysError.Header.DATABASE_ERROR.getName())
                .build();
        ResponseEntity<Object> mockResponseEntity = new ResponseEntity<>(mockAysError, HttpStatus.INTERNAL_SERVER_ERROR);

        // Then
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleDataAccessException(mockException);
        Assertions.assertEquals(mockResponseEntity.getStatusCode(), responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getHeaders());
        Assertions.assertNotNull(responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());

    }

    private void checkAysError(AysError mockAysError, AysError aysError) {
        Assertions.assertNotNull(aysError.getTime());
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