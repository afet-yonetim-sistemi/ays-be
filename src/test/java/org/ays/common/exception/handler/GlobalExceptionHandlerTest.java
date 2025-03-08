package org.ays.common.exception.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import org.ays.AysRestControllerTest;
import org.ays.common.exception.AysAuthException;
import org.ays.common.exception.AysForbiddenException;
import org.ays.common.exception.AysNotExistException;
import org.ays.common.exception.AysProcessException;
import org.ays.common.model.response.AysErrorResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.Serial;
import java.lang.reflect.Method;
import java.sql.SQLException;

class GlobalExceptionHandlerTest extends AysRestControllerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    @SuppressWarnings("ConstantConditions")
    void givenInvalidArgumentToAnyEndpoint_whenThrowMethodArgumentTypeMismatchException_thenReturnAysError() {
        // Given
        MethodArgumentTypeMismatchException mockException = new MethodArgumentTypeMismatchException("test", String.class, "username", null, null);

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.subErrors(mockException)
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleValidationErrors(mockException);
        this.checkAysError(mockErrorResponse, errorResponse);

    }

    @Test
    void givenInvalidArgumentToAnyEndpoint_whenThrowMethodArgumentNotValidException_thenReturnAysError() throws NoSuchMethodException {

        // Given
        MethodArgumentNotValidException mockException = this.getMethodArgumentNotValidException();

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.subErrors(mockException.getBindingResult().getFieldErrors())
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleValidationErrors(mockException);
        this.checkAysError(mockErrorResponse, errorResponse);
    }

    private @NotNull MethodArgumentNotValidException getMethodArgumentNotValidException() throws NoSuchMethodException {
        Method method = GlobalExceptionHandlerTest.class.getDeclaredMethod("givenInvalidArgumentToAnyEndpoint_whenThrowMethodArgumentNotValidException_thenReturnAysError");
        int parameterIndex = -1;

        MethodParameter mockParameter = new MethodParameter(method, parameterIndex);
        BindingResult mockBindingResult = new BeanPropertyBindingResult(null, "");
        return new MethodArgumentNotValidException(mockParameter, mockBindingResult);
    }

    @Test
    void givenInvalidArgumentToAnyEndpoint_whenThrowConstraintViolationException_thenReturnAysError() {

        // Given
        ConstraintViolationException mockException = new ConstraintViolationException(null);

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.subErrors(mockException.getConstraintViolations())
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handlePathVariableErrors(mockException);
        this.checkAysError(mockErrorResponse, errorResponse);
    }

    @Test
    void givenResourceNotFound_whenThrowAysNotExistException_thenReturnAysErrorWithReturnAysError() {

        // Given
        AysNotExistException mockException = new AysNotExistException("Resource not found") {

            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public String getMessage() {
                return "Resource not found";
            }
        };

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.NOT_EXIST_ERROR.getName())
                .message(mockException.getMessage())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleNotExistError(mockException);
        this.checkAysError(mockErrorResponse, errorResponse);
    }

    @Test
    void givenInternalServerException_whenThrowAysProcessException_WithReturnAysError() {

        // Given
        AysProcessException mockException = new AysProcessException("Internal server error") {

            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public String getMessage() {
                return "Internal server error";
            }
        };

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.PROCESS_ERROR.getName())
                .message(mockException.getMessage())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleProcessError(mockException);
        this.checkAysError(mockErrorResponse, errorResponse);
    }

    @Test
    void givenHandleEndpointNotFoundException_whenThrowNoResourceFoundException_thenReturnAysError() {

        // Given
        HttpMethod[] httpMethods = HttpMethod.values();

        for (HttpMethod method : httpMethods) {
            NoResourceFoundException mockException = new NoResourceFoundException(method, "Resource not found");

            // When
            AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                    .header(AysErrorResponse.Header.API_ERROR.getName())
                    .build();

            // Then
            AysErrorResponse errorResponse = globalExceptionHandler.handleEndpointNotFoundError(mockException);
            this.checkAysError(mockErrorResponse, errorResponse);
        }
    }


    @Test
    void givenForbiddenException_whenThrowForbiddenException_thenReturnAysError() {

        // Given
        AysForbiddenException mockException = new AysForbiddenException("Forbidden action") {

            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public String getMessage() {
                return "Forbidden action";
            }
        };

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.AUTH_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleForbiddenError(mockException);
        this.checkAysError(mockErrorResponse, errorResponse);
    }


    @Test
    void givenAccessDeniedException_whenThrowAccessDeniedException_thenReturnAysError() {

        // Given
        AccessDeniedException mockException = new AccessDeniedException("Access denied");

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.AUTH_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleAccessDeniedError(mockException);
        this.checkAysError(mockErrorResponse, errorResponse);
    }

    @Test
    void givenSQLException_whenThrowSQLException_thenReturnAysError() {

        // Given
        SQLException mockException = new SQLException("Database error");

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.DATABASE_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleSQLError(mockException);
        this.checkAysError(mockErrorResponse, errorResponse);
    }

    @Test
    void givenAuthenticationFailure_whenThrowAysAuthException_thenReturnAysError() {
        // Given
        AysAuthException mockException = new AysAuthException("Authentication failed") {

            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public String getMessage() {
                return "Authentication failed";
            }
        };

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.AUTH_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleAuthError(mockException);
        this.checkAysError(mockErrorResponse, errorResponse);
    }

    @Test
    void givenUnsupportedHttpMethod_whenThrowHttpRequestMethodNotSupportedException_thenReturnAysError() {

        // Given
        HttpRequestMethodNotSupportedException mockException = new HttpRequestMethodNotSupportedException("Unsupported method");

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.API_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleHttpRequestMethodNotSupportedException(mockException);
        this.checkAysError(mockErrorResponse, errorResponse);
    }

    @Test
    void givenUnsupportedMediaType_whenThrowHttpMediaTypeNotSupportedException_thenReturnAysError() {

        // Given
        HttpMediaTypeNotSupportedException mockException = new HttpMediaTypeNotSupportedException("Unsupported media type");

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.API_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleHttpMediaTypeNotSupportedException(mockException);
        this.checkAysError(mockErrorResponse, errorResponse);
    }

    @Test
    void givenDataAccessException_whenThrowDataAccessException_thenReturnAysErrorWithInternalServerErrorStatus() {

        // Given
        DataAccessException mockException = new DataAccessException("Data access error") {

            @Serial
            private static final long serialVersionUID = 1L;
        };

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.DATABASE_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleDataAccessException(mockException);
        this.checkAysError(mockErrorResponse, errorResponse);
    }

    @Test
    void givenJsonParseError_whenThrowHttpMessageNotReadableException_thenReturnAysError() {

        // Given
        HttpInputMessage mockHttpInputMessage = new MockHttpInputMessage(new byte[]{});
        HttpMessageNotReadableException mockException = new HttpMessageNotReadableException("Invalid JSON", mockHttpInputMessage);

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleJsonParseErrors(mockException);
        this.checkAysError(mockErrorResponse, errorResponse);
    }

    @Test
    @SuppressWarnings("deprecation")
    void givenInvalidJsonFormat_whenThrowHttpMessageNotReadableException_thenReturnAysErrorResponse() {

        // Given
        InvalidFormatException mockInvalidFormatException = InvalidFormatException.from(null, "Invalid format", null, String.class);
        JsonMappingException.Reference mockReference = new JsonMappingException.Reference("testObject", "testField");
        mockInvalidFormatException.prependPath(mockReference);
        HttpMessageNotReadableException mockException = new HttpMessageNotReadableException("Invalid JSON", mockInvalidFormatException);

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.subErrors(mockInvalidFormatException)
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleJsonParseErrors(mockException);
        this.checkAysError(mockErrorResponse, errorResponse);
    }

    private void checkAysError(AysErrorResponse mockErrorResponse, AysErrorResponse errorResponse) {
        Assertions.assertNotNull(errorResponse.getTime());
        Assertions.assertNotNull(errorResponse.getCode());
        Assertions.assertEquals(mockErrorResponse.getHeader(), errorResponse.getHeader());
        Assertions.assertEquals(mockErrorResponse.getIsSuccess(), errorResponse.getIsSuccess());

        if (mockErrorResponse.getMessage() != null) {
            Assertions.assertEquals(mockErrorResponse.getMessage(), errorResponse.getMessage());
        }

        if (mockErrorResponse.getSubErrors() != null) {
            Assertions.assertEquals(mockErrorResponse.getSubErrors().size(), errorResponse.getSubErrors().size());
            Assertions.assertEquals(mockErrorResponse.getSubErrors().get(0).getMessage(), errorResponse.getSubErrors().get(0).getMessage());
            Assertions.assertEquals(mockErrorResponse.getSubErrors().get(0).getField(), errorResponse.getSubErrors().get(0).getField());
            Assertions.assertEquals(mockErrorResponse.getSubErrors().get(0).getValue(), errorResponse.getSubErrors().get(0).getValue());
            Assertions.assertEquals(mockErrorResponse.getSubErrors().get(0).getType(), errorResponse.getSubErrors().get(0).getType());
        }

    }

}
