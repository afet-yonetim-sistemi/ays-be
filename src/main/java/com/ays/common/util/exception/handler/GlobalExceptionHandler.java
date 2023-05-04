package com.ays.common.util.exception.handler;

import com.ays.common.util.exception.AysAlreadyException;
import com.ays.common.util.exception.AysAuthException;
import com.ays.common.util.exception.AysNotExistException;
import com.ays.common.util.exception.AysProcessException;
import com.ays.common.util.exception.model.AysError;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleValidationErrors(final MethodArgumentTypeMismatchException exception) {

        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.subErrors(exception)
                .header(AysError.Header.VALIDATION_ERROR.getName())
                .build();
        return new ResponseEntity<>(aysError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleValidationErrors(final MethodArgumentNotValidException exception) {

        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.subErrors(exception.getBindingResult().getFieldErrors())
                .header(AysError.Header.VALIDATION_ERROR.getName())
                .build();
        return new ResponseEntity<>(aysError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handlePathVariableErrors(final ConstraintViolationException exception) {
        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.subErrors(exception.getConstraintViolations())
                .header(AysError.Header.VALIDATION_ERROR.getName())
                .build();
        return new ResponseEntity<>(aysError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AysNotExistException.class)
    protected ResponseEntity<Object> handleNotExistError(final AysNotExistException exception) {
        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.builder()
                .header(AysError.Header.NOT_FOUND.getName())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(aysError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AysAlreadyException.class)
    protected ResponseEntity<Object> handleAlreadyExistError(final AysAlreadyException exception) {
        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.builder()
                .header(AysError.Header.ALREADY_EXIST.getName())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(aysError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AysProcessException.class)
    protected ResponseEntity<Object> handleProcessError(final AysProcessException exception) {
        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.builder()
                .header(AysError.Header.PROCESS_ERROR.getName())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(aysError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AysAuthException.class)
    protected ResponseEntity<Object> handleAuthError(final AysAuthException exception) {
        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.builder()
                .header(AysError.Header.AUTH_ERROR.getName())
                .build();
        return new ResponseEntity<>(aysError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SQLException.class)
    protected ResponseEntity<Object> handleSQLError(final SQLException exception) {
        log.error(exception.getMessage(), exception);

        AysError aysError = AysError.builder()
                .header(AysError.Header.DATABASE_ERROR.getName())
                .build();
        return new ResponseEntity<>(aysError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
