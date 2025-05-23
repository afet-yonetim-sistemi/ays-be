package org.ays.common.exception.handler;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import org.ays.AysUnitTest;
import org.ays.common.exception.AysAuthException;
import org.ays.common.exception.AysConflictException;
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
import java.util.Optional;

class GlobalExceptionHandlerTest extends AysUnitTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    @SuppressWarnings("ConstantConditions")
    void givenMethodArgumentTypeMismatchException_whenValidationErrorsHandled_thenReturnErrorResponse() {
        // Given
        MethodArgumentTypeMismatchException mockException = new MethodArgumentTypeMismatchException("test", String.class, "username", null, null);

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.subErrors(mockException)
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleValidationErrors(mockException);
        this.validateErrorResponse(mockErrorResponse, errorResponse);
        this.validateConsoleLog(mockException, errorResponse);
    }

    @Test
    void givenMethodArgumentNotValidException_whenValidationErrorsHandled_thenReturnErrorResponse() throws NoSuchMethodException {

        // Given
        Method method = GlobalExceptionHandlerTest.class.getDeclaredMethod("givenMethodArgumentNotValidException_whenValidationErrorsHandled_thenReturnErrorResponse");
        int parameterIndex = -1;

        MethodParameter mockParameter = new MethodParameter(method, parameterIndex);
        BindingResult mockBindingResult = new BeanPropertyBindingResult(null, "");
        MethodArgumentNotValidException mockException = new MethodArgumentNotValidException(mockParameter, mockBindingResult);

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.subErrors(mockException.getBindingResult().getFieldErrors())
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleValidationErrors(mockException);
        this.validateErrorResponse(mockErrorResponse, errorResponse);
        this.validateConsoleLog(mockException, errorResponse);
    }

    @Test
    void givenConstraintViolationException_whenPathVariableErrorsHandle_thenReturnErrorResponse() {

        // Given
        ConstraintViolationException mockException = new ConstraintViolationException(null);

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.subErrors(mockException.getConstraintViolations())
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handlePathVariableErrors(mockException);
        this.validateErrorResponse(mockErrorResponse, errorResponse);
        this.validateConsoleLog(mockException, errorResponse);
    }

    @Test
    void givenAysNotExistException_whenNotExistErrorHandled_thenReturnErrorResponse() {

        // Given
        AysNotExistException mockException = new AysNotExistException("user does not exist") {

            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public String getMessage() {
                return "user does not exist";
            }
        };

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.NOT_EXIST_ERROR.getName())
                .message(mockException.getMessage())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleNotExistError(mockException);
        this.validateErrorResponse(mockErrorResponse, errorResponse);
        this.validateConsoleLog(mockException, errorResponse);
    }

    @Test
    void givenAysConflictException_whenConflictErrorHandled_thenReturnErrorResponse() {

        // Given
        AysConflictException mockException = new AysConflictException("User already exists") {

            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public String getMessage() {
                return "User already exists";
            }
        };

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.CONFLICT_ERROR.getName())
                .message(mockException.getMessage())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleConflictError(mockException);
        this.validateErrorResponse(mockErrorResponse, errorResponse);
        this.validateConsoleLog(mockException, errorResponse);
    }

    @Test
    void givenAysProcessException_whenProcessErrorHandled_thenReturnErrorResponse() {

        // Given
        AysProcessException mockException = new AysProcessException("AYS server error") {

            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public String getMessage() {
                return "AYS server error";
            }
        };

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.PROCESS_ERROR.getName())
                .message(mockException.getMessage())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleProcessError(mockException);
        this.validateErrorResponse(mockErrorResponse, errorResponse);
        this.validateConsoleLog(mockException, errorResponse);
    }

    @Test
    void givenNullPointerException_whenProcessErrorHandled_thenReturnErrorResponse() {

        // Given
        NullPointerException mockException = new NullPointerException("null") {

            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public String getMessage() {
                return "null";
            }
        };

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.PROCESS_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleProcessError(mockException);
        this.validateErrorResponse(mockErrorResponse, errorResponse);
        this.validateConsoleLog(mockException, errorResponse);
    }

    @Test
    void givenNoResourceFoundException_whenEndpointNotFoundErrorHandled_thenReturnErrorResponse() {

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
            this.validateErrorResponse(mockErrorResponse, errorResponse);
            this.validateConsoleLog(mockException, errorResponse);
        }
    }


    @Test
    void givenAysForbiddenException_whenForbiddenErrorHandled_thenReturnErrorResponse() {

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
        this.validateErrorResponse(mockErrorResponse, errorResponse);
        this.validateConsoleLog(mockException, errorResponse);
    }


    @Test
    void givenAccessDeniedException_whenAccessDeniedErrorHandle_thenReturnErrorResponse() {

        // Given
        AccessDeniedException mockException = new AccessDeniedException("Access denied");

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.AUTH_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleAccessDeniedError(mockException);
        this.validateErrorResponse(mockErrorResponse, errorResponse);
        this.validateConsoleLog(mockException, errorResponse);
    }

    @Test
    void givenSQLException_whenSQLErrorHandled_thenReturnErrorResponse() {

        // Given
        SQLException mockException = new SQLException("Database error");

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.DATABASE_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleSQLError(mockException);
        this.validateErrorResponse(mockErrorResponse, errorResponse);
        this.validateConsoleLog(mockException, errorResponse);
    }

    @Test
    void givenAysAuthException_whenAuthErrorHandled_thenReturnErrorResponse() {
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
        this.validateErrorResponse(mockErrorResponse, errorResponse);
        this.validateConsoleLog(mockException, errorResponse);
    }

    @Test
    void givenHttpRequestMethodNotSupportedException_whenMethodNotAllowedErrorHandled_thenReturnErrorResponse() {

        // Given
        HttpRequestMethodNotSupportedException mockException = new HttpRequestMethodNotSupportedException("Method not allowed");

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.API_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleMethodNotAllowedError(mockException);
        this.validateErrorResponse(mockErrorResponse, errorResponse);
        this.validateConsoleLog(mockException, errorResponse);
    }

    @Test
    void givenHttpMediaTypeNotSupportedException_whenUnsupportedMediaTypeErrorHandled_thenReturnErrorResponse() {

        // Given
        HttpMediaTypeNotSupportedException mockException = new HttpMediaTypeNotSupportedException("Unsupported media type");

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.API_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleUnsupportedMediaTypeError(mockException);
        this.validateErrorResponse(mockErrorResponse, errorResponse);
        this.validateConsoleLog(mockException, errorResponse);
    }

    @Test
    void givenDataAccessException_whenDataAccessErrorHandled_thenReturnErrorResponse() {

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
        AysErrorResponse errorResponse = globalExceptionHandler.handleDataAccessError(mockException);
        this.validateErrorResponse(mockErrorResponse, errorResponse);
        this.validateConsoleLog(mockException, errorResponse);
    }

    @Test
    void givenHttpInputMessage_whenJsonParseErrorsHandled_thenReturnErrorResponse() {

        // Given
        HttpInputMessage mockHttpInputMessage = new MockHttpInputMessage(new byte[]{});
        HttpMessageNotReadableException mockException = new HttpMessageNotReadableException("Invalid JSON", mockHttpInputMessage);

        // When
        AysErrorResponse mockErrorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();

        // Then
        AysErrorResponse errorResponse = globalExceptionHandler.handleJsonParseErrors(mockException);
        this.validateErrorResponse(mockErrorResponse, errorResponse);
        this.validateConsoleLog(mockException, errorResponse);
    }

    @Test
    @SuppressWarnings("deprecation")
    void givenInvalidFormatException_whenJsonParseErrorsHandled_thenReturnErrorResponse() {

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
        this.validateErrorResponse(mockErrorResponse, errorResponse);
        this.validateConsoleLog(mockException, errorResponse);
    }

    private void validateErrorResponse(AysErrorResponse mockErrorResponse, AysErrorResponse errorResponse) {

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

    private void validateConsoleLog(Exception mockException, AysErrorResponse errorResponse) {

        String logMessagePrefix = "responseCode:" + errorResponse.getCode();
        Optional<String> errorLog = logTracker.findMessage(Level.ERROR, logMessagePrefix);

        Assertions.assertTrue(errorLog.isPresent());
        Assertions.assertEquals(logMessagePrefix + " | " + mockException.getMessage(), errorLog.get());

        Optional<String> traceLog = logTracker.findMessage(Level.TRACE, logMessagePrefix);

        Assertions.assertTrue(traceLog.isPresent());
        Assertions.assertEquals(logMessagePrefix + " | StackTrace:", traceLog.get());
    }

}
