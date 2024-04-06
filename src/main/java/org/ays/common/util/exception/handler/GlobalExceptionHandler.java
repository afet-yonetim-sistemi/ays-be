package org.ays.common.util.exception.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.ays.common.util.exception.AysAlreadyException;
import org.ays.common.util.exception.AysAuthException;
import org.ays.common.util.exception.AysNotExistException;
import org.ays.common.util.exception.AysProcessException;
import org.ays.common.util.exception.model.AysErrorResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;

/**
 * Global exception handler acting as controller advice for certain use cases happened in the controller.
 */
@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<Object> handleJsonParseErrors(final HttpMessageNotReadableException exception) {
        log.error(exception.getMessage(), exception);

        AysErrorResponse errorResponse = AysErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<Object> handleValidationErrors(final MethodArgumentTypeMismatchException exception) {

        log.error(exception.getMessage(), exception);

        AysErrorResponse errorResponse = AysErrorResponse.subErrors(exception)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Object> handleValidationErrors(final MethodArgumentNotValidException exception) {

        log.error(exception.getMessage(), exception);

        AysErrorResponse errorResponse = AysErrorResponse.subErrors(exception.getBindingResult().getFieldErrors())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Object> handlePathVariableErrors(final ConstraintViolationException exception) {
        log.error(exception.getMessage(), exception);

        AysErrorResponse errorResponse = AysErrorResponse.subErrors(exception.getConstraintViolations())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AysNotExistException.class)
    ResponseEntity<Object> handleNotExistError(final AysNotExistException exception) {
        log.error(exception.getMessage(), exception);

        AysErrorResponse errorResponse = AysErrorResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .header(AysErrorResponse.Header.NOT_FOUND.getName())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AysAlreadyException.class)
    ResponseEntity<Object> handleAlreadyExistError(final AysAlreadyException exception) {
        log.error(exception.getMessage(), exception);

        AysErrorResponse errorResponse = AysErrorResponse.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .header(AysErrorResponse.Header.ALREADY_EXIST.getName())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AysProcessException.class)
    ResponseEntity<Object> handleProcessError(final AysProcessException exception) {
        log.error(exception.getMessage(), exception);

        AysErrorResponse errorResponse = AysErrorResponse.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(AysErrorResponse.Header.PROCESS_ERROR.getName())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Object> handleProcessError(final Exception exception) {
        log.error(exception.getMessage(), exception);

        AysErrorResponse errorResponse = AysErrorResponse.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(AysErrorResponse.Header.PROCESS_ERROR.getName())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AysAuthException.class)
    ResponseEntity<Object> handleAuthError(final AysAuthException exception) {
        log.error(exception.getMessage(), exception);

        AysErrorResponse errorResponse = AysErrorResponse.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .header(AysErrorResponse.Header.AUTH_ERROR.getName())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<Object> handleAccessDeniedError(final AccessDeniedException exception) {
        log.error(exception.getMessage(), exception);

        AysErrorResponse errorResponse = AysErrorResponse.builder()
                .httpStatus(HttpStatus.FORBIDDEN)
                .header(AysErrorResponse.Header.AUTH_ERROR.getName())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SQLException.class)
    ResponseEntity<Object> handleSQLError(final SQLException exception) {
        log.error(exception.getMessage(), exception);

        AysErrorResponse errorResponse = AysErrorResponse.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(AysErrorResponse.Header.DATABASE_ERROR.getName())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        log.error(exception.getMessage(), exception);

        AysErrorResponse errorResponse = AysErrorResponse.builder()
                .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    ResponseEntity<Object> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {

        log.error(exception.getMessage(), exception);

        AysErrorResponse errorResponse = AysErrorResponse.builder()
                .httpStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(DataAccessException.class)
    ResponseEntity<Object> handleDataAccessException(DataAccessException exception) {

        log.error(exception.getMessage(), exception);

        AysErrorResponse errorResponse = AysErrorResponse.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(AysErrorResponse.Header.DATABASE_ERROR.getName())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
