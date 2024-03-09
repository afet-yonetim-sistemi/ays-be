package org.ays.common.util.exception.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.ays.common.util.exception.AysAlreadyException;
import org.ays.common.util.exception.AysAuthException;
import org.ays.common.util.exception.AysNotExistException;
import org.ays.common.util.exception.AysProcessException;
import org.ays.common.util.exception.model.AysError;
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

        AysError aysError = AysError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(AysError.Header.VALIDATION_ERROR.getName())
                .build();
        return new ResponseEntity<>(aysError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<Object> handleValidationErrors(final MethodArgumentTypeMismatchException exception) {

        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.subErrors(exception)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(AysError.Header.VALIDATION_ERROR.getName())
                .build();
        return new ResponseEntity<>(aysError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Object> handleValidationErrors(final MethodArgumentNotValidException exception) {

        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.subErrors(exception.getBindingResult().getFieldErrors())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(AysError.Header.VALIDATION_ERROR.getName())
                .build();
        return new ResponseEntity<>(aysError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Object> handlePathVariableErrors(final ConstraintViolationException exception) {
        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.subErrors(exception.getConstraintViolations())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(AysError.Header.VALIDATION_ERROR.getName())
                .build();
        return new ResponseEntity<>(aysError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AysNotExistException.class)
    ResponseEntity<Object> handleNotExistError(final AysNotExistException exception) {
        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .header(AysError.Header.NOT_FOUND.getName())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(aysError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AysAlreadyException.class)
    ResponseEntity<Object> handleAlreadyExistError(final AysAlreadyException exception) {
        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .header(AysError.Header.ALREADY_EXIST.getName())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(aysError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AysProcessException.class)
    ResponseEntity<Object> handleProcessError(final AysProcessException exception) {
        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(AysError.Header.PROCESS_ERROR.getName())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(aysError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Object> handleProcessError(final Exception exception) {
        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(AysError.Header.PROCESS_ERROR.getName())
                .build();
        return new ResponseEntity<>(aysError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AysAuthException.class)
    ResponseEntity<Object> handleAuthError(final AysAuthException exception) {
        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .header(AysError.Header.AUTH_ERROR.getName())
                .build();
        return new ResponseEntity<>(aysError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<Object> handleAccessDeniedError(final AccessDeniedException exception) {
        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.builder()
                .httpStatus(HttpStatus.FORBIDDEN)
                .header(AysError.Header.AUTH_ERROR.getName())
                .build();
        return new ResponseEntity<>(aysError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SQLException.class)
    ResponseEntity<Object> handleSQLError(final SQLException exception) {
        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(AysError.Header.DATABASE_ERROR.getName())
                .build();
        return new ResponseEntity<>(aysError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.builder()
                .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                .header(AysError.Header.VALIDATION_ERROR.getName())
                .build();

        return new ResponseEntity<>(aysError, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    ResponseEntity<Object> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {

        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.builder()
                .httpStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .header(AysError.Header.VALIDATION_ERROR.getName())
                .build();

        return new ResponseEntity<>(aysError, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(DataAccessException.class)
    ResponseEntity<Object> handleDataAccessException(DataAccessException exception) {

        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(AysError.Header.DATABASE_ERROR.getName())
                .build();

        return new ResponseEntity<>(aysError, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
