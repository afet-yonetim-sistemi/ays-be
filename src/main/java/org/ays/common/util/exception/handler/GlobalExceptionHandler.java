package org.ays.common.util.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.ays.auth.util.exception.AysUserNotSuperAdminException;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.util.exception.AysAlreadyException;
import org.ays.common.util.exception.AysAuthException;
import org.ays.common.util.exception.AysBadRequestException;
import org.ays.common.util.exception.AysNotExistException;
import org.ays.common.util.exception.AysProcessException;
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
class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    AysErrorResponse handleJsonParseErrors(final HttpMessageNotReadableException exception) {
        log.error(exception.getMessage(), exception);

        if (exception.getCause() instanceof InvalidFormatException invalidFormatException) {
            return AysErrorResponse.subErrors(invalidFormatException)
                    .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                    .build();
        }

        return AysErrorResponse.builder()
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    AysErrorResponse handleValidationErrors(final MethodArgumentTypeMismatchException exception) {

        log.error(exception.getMessage(), exception);

        return AysErrorResponse.subErrors(exception)
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    AysErrorResponse handleValidationErrors(final MethodArgumentNotValidException exception) {

        log.error(exception.getMessage(), exception);

        return AysErrorResponse.subErrors(exception.getBindingResult().getFieldErrors())
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    AysErrorResponse handlePathVariableErrors(final ConstraintViolationException exception) {
        log.error(exception.getMessage(), exception);

        return AysErrorResponse.subErrors(exception.getConstraintViolations())
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();
    }

    @ExceptionHandler(AysNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    AysErrorResponse handleNotExistError(final AysNotExistException exception) {
        log.error(exception.getMessage(), exception);

        return AysErrorResponse.builder()
                .header(AysErrorResponse.Header.NOT_FOUND.getName())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(AysBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    AysErrorResponse handleBadRequestError(final AysBadRequestException exception) {
        log.error(exception.getMessage(), exception);

        return AysErrorResponse.builder()
                .header(AysErrorResponse.Header.BAD_REQUEST.getName())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(AysAlreadyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    AysErrorResponse handleAlreadyExistError(final AysAlreadyException exception) {
        log.error(exception.getMessage(), exception);

        return AysErrorResponse.builder()
                .header(AysErrorResponse.Header.ALREADY_EXIST.getName())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(AysProcessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    AysErrorResponse handleProcessError(final AysProcessException exception) {
        log.error(exception.getMessage(), exception);

        return AysErrorResponse.builder()
                .header(AysErrorResponse.Header.PROCESS_ERROR.getName())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    AysErrorResponse handleProcessError(final Exception exception) {
        log.error(exception.getMessage(), exception);

        return AysErrorResponse.builder()
                .header(AysErrorResponse.Header.PROCESS_ERROR.getName())
                .build();
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    AysErrorResponse handleEndpointNotFoundError(final NoResourceFoundException exception) {
        log.error(exception.getMessage(), exception);

        return AysErrorResponse.builder()
                .header(AysErrorResponse.Header.API_ERROR.getName())
                .build();
    }

    @ExceptionHandler(AysAuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    AysErrorResponse handleAuthError(final AysAuthException exception) {
        log.error(exception.getMessage(), exception);

        return AysErrorResponse.builder()
                .header(AysErrorResponse.Header.AUTH_ERROR.getName())
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    AysErrorResponse handleAccessDeniedError(final AccessDeniedException exception) {
        log.error(exception.getMessage(), exception);

        return AysErrorResponse.builder()
                .header(AysErrorResponse.Header.AUTH_ERROR.getName())
                .build();
    }

    @ExceptionHandler(AysUserNotSuperAdminException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    AysErrorResponse handleAysUserNotSuperAdminError(final AysUserNotSuperAdminException exception) {
        log.error(exception.getMessage(), exception);

        return AysErrorResponse.builder()
                .header(AysErrorResponse.Header.AUTH_ERROR.getName())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    AysErrorResponse handleSQLError(final SQLException exception) {
        log.error(exception.getMessage(), exception);

        return AysErrorResponse.builder()
                .header(AysErrorResponse.Header.DATABASE_ERROR.getName())
                .build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    AysErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        log.error(exception.getMessage(), exception);

        return AysErrorResponse.builder()
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    AysErrorResponse handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {

        log.error(exception.getMessage(), exception);

        return AysErrorResponse.builder()
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    AysErrorResponse handleDataAccessException(DataAccessException exception) {

        log.error(exception.getMessage(), exception);

        return AysErrorResponse.builder()
                .header(AysErrorResponse.Header.DATABASE_ERROR.getName())
                .build();
    }

}
