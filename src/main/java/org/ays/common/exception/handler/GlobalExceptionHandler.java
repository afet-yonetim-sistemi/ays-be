package org.ays.common.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.ays.common.exception.AysAuthException;
import org.ays.common.exception.AysConflictException;
import org.ays.common.exception.AysForbiddenException;
import org.ays.common.exception.AysNotExistException;
import org.ays.common.exception.AysProcessException;
import org.ays.common.model.response.AysErrorResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLException;

/**
 * Global exception handler acting as controller advice for certain use cases happened in the controller.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    AysErrorResponse handleJsonParseErrors(final HttpMessageNotReadableException exception) {

        if (exception.getCause() instanceof InvalidFormatException invalidFormatException) {

            final AysErrorResponse errorResponse = AysErrorResponse.subErrors(invalidFormatException)
                    .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                    .build();

            this.logException(exception, errorResponse);

            return errorResponse;
        }

        final AysErrorResponse errorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();

        this.logException(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    AysErrorResponse handleValidationErrors(final MethodArgumentTypeMismatchException exception) {

        final AysErrorResponse errorResponse = AysErrorResponse.subErrors(exception)
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();

        this.logException(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    AysErrorResponse handleValidationErrors(final MethodArgumentNotValidException exception) {

        final AysErrorResponse errorResponse = AysErrorResponse.subErrors(exception.getBindingResult().getFieldErrors())
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();

        this.logException(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    AysErrorResponse handlePathVariableErrors(final ConstraintViolationException exception) {

        final AysErrorResponse errorResponse = AysErrorResponse.subErrors(exception.getConstraintViolations())
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();

        this.logException(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(AysNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    AysErrorResponse handleNotExistError(final AysNotExistException exception) {

        final AysErrorResponse errorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.NOT_EXIST_ERROR.getName())
                .message(exception.getMessage())
                .build();

        this.logException(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(AysConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    AysErrorResponse handleConflictError(final AysConflictException exception) {

        final AysErrorResponse errorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.CONFLICT_ERROR.getName())
                .message(exception.getMessage())
                .build();

        this.logException(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(AysProcessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    AysErrorResponse handleProcessError(final AysProcessException exception) {

        final AysErrorResponse errorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.PROCESS_ERROR.getName())
                .message(exception.getMessage())
                .build();

        this.logException(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    AysErrorResponse handleProcessError(final Exception exception) {

        final AysErrorResponse errorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.PROCESS_ERROR.getName())
                .build();

        this.logException(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    AysErrorResponse handleEndpointNotFoundError(final NoResourceFoundException exception) {

        final AysErrorResponse errorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.API_ERROR.getName())
                .build();

        this.logException(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(AysAuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    AysErrorResponse handleAuthError(final AysAuthException exception) {

        final AysErrorResponse errorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.AUTH_ERROR.getName())
                .build();

        this.logException(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(AysForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    AysErrorResponse handleForbiddenError(final AysForbiddenException exception) {

        final AysErrorResponse errorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.AUTH_ERROR.getName())
                .message(exception.getMessage())
                .build();

        this.logException(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    AysErrorResponse handleAccessDeniedError(final AccessDeniedException exception) {

        final AysErrorResponse errorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.AUTH_ERROR.getName())
                .build();

        this.logException(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    AysErrorResponse handleSQLError(final SQLException exception) {

        final AysErrorResponse errorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.DATABASE_ERROR.getName())
                .build();

        this.logException(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    AysErrorResponse handleMethodNotAllowedError(HttpRequestMethodNotSupportedException exception) {

        final AysErrorResponse errorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.API_ERROR.getName())
                .build();

        this.logException(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    AysErrorResponse handleUnsupportedMediaTypeError(HttpMediaTypeNotSupportedException exception) {

        final AysErrorResponse errorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.API_ERROR.getName())
                .build();

        this.logException(exception, errorResponse);

        return errorResponse;
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    AysErrorResponse handleDataAccessError(DataAccessException exception) {

        final AysErrorResponse errorResponse = AysErrorResponse.builder()
                .header(AysErrorResponse.Header.DATABASE_ERROR.getName())
                .build();

        this.logException(exception, errorResponse);

        return errorResponse;
    }


    private void logException(final Exception exception,
                              final AysErrorResponse errorResponse) {

        final String responseCode = errorResponse.getCode();
        log.error("responseCode:{} | {}", responseCode, exception.getMessage());
        log.trace("responseCode:{} | StackTrace:", responseCode, exception);
    }

}
