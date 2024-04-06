package org.ays.common.util.exception.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * AysError class represents a standard error response object with error details and sub-errors.
 * It is used to provide consistent and meaningful error responses for API calls.
 */
@Getter
@Builder
public class AysErrorResponse {

    /**
     * The time when the error occurred.
     */
    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();

    /**
     * The header of the error response.
     */
    private String header;

    /**
     * The message describing the error.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    /**
     * Indicates if the API call was successful or not.
     */
    @Builder.Default
    private final Boolean isSuccess = false;

    /**
     * List of sub-errors.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SubError> subErrors;

    /**
     * SubError class represents a sub-error with its details.
     */
    @Getter
    @Builder
    public static class SubError {
        /**
         * The error message.
         */
        private String message;
        /**
         * The field that caused the error.
         */
        private String field;
        /**
         * The value of the field that caused the error.
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Object value;
        /**
         * The type of the error.
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String type;
    }

    /**
     * Header enum represents the different types of error headers.
     */
    @Getter
    @RequiredArgsConstructor
    public enum Header {
        /**
         * API_ERROR header.
         */
        API_ERROR("API ERROR"),
        /**
         * ALREADY_EXIST header.
         */
        ALREADY_EXIST("ALREADY EXIST"),
        /**
         * NOT_FOUND header.
         */
        NOT_FOUND("NOT EXIST"),
        /**
         * VALIDATION_ERROR header.
         */
        VALIDATION_ERROR("VALIDATION ERROR"),
        /**
         * DATABASE_ERROR header.
         */
        DATABASE_ERROR("DATABASE ERROR"),
        /**
         * PROCESS_ERROR header.
         */
        PROCESS_ERROR("PROCESS ERROR"),
        /**
         * AUTH_ERROR header.
         */
        AUTH_ERROR("AUTH ERROR");

        /**
         * The name of the header.
         */
        private final String name;
    }


    /**
     * A static method that creates an {@link AysErrorResponseBuilder} instance with the given list of {@link FieldError} objects
     * as sub-errors.
     *
     * @param fieldErrors a {@link List} of {@link FieldError} objects to be used as sub-errors in the {@link AysErrorResponse} instance
     * @return an instance of {@link AysErrorResponseBuilder} with the given list of {@link FieldError} objects as sub-errors
     */
    public static AysErrorResponse.AysErrorResponseBuilder subErrors(final List<FieldError> fieldErrors) {

        if (CollectionUtils.isEmpty(fieldErrors)) {
            return AysErrorResponse.builder();
        }

        final List<SubError> subErrorErrors = new ArrayList<>();

        fieldErrors.forEach(fieldError -> {
            final SubError.SubErrorBuilder sisSubErrorBuilder = SubError.builder();

            List<String> codes = List.of(Objects.requireNonNull(fieldError.getCodes()));
            if (!codes.isEmpty()) {

                sisSubErrorBuilder.field(StringUtils.substringAfterLast(codes.get(0), "."));

                if (!"AssertTrue".equals(codes.get(codes.size() - 1))) {
                    sisSubErrorBuilder.type(StringUtils.substringAfterLast(codes.get(codes.size() - 2), "."));
                }
            }
            sisSubErrorBuilder.value(fieldError.getRejectedValue() != null ? fieldError.getRejectedValue().toString() : null);
            sisSubErrorBuilder.message(fieldError.getDefaultMessage());

            subErrorErrors.add(sisSubErrorBuilder.build());
        });

        return AysErrorResponse.builder().subErrors(subErrorErrors);
    }

    /**
     * A static method that creates an {@link AysErrorResponseBuilder} instance with the given set of {@link ConstraintViolation} objects
     * as sub-errors.
     *
     * @param constraintViolations a {@link Set} of {@link ConstraintViolation} objects to be used as sub-errors in the {@link AysErrorResponse} instance
     * @return an instance of {@link AysErrorResponseBuilder} with the given set of {@link ConstraintViolation} objects as sub-errors
     */
    public static AysErrorResponse.AysErrorResponseBuilder subErrors(final Set<ConstraintViolation<?>> constraintViolations) {

        if (CollectionUtils.isEmpty(constraintViolations)) {
            return AysErrorResponse.builder();
        }

        final List<SubError> subErrors = new ArrayList<>();

        constraintViolations.forEach(constraintViolation ->
                subErrors.add(
                        SubError.builder()
                                .message(constraintViolation.getMessage())
                                .field(StringUtils.substringAfterLast(constraintViolation.getPropertyPath().toString(), "."))
                                .value(constraintViolation.getInvalidValue() != null ? constraintViolation.getInvalidValue().toString() : null)
                                .type(constraintViolation.getInvalidValue().getClass().getSimpleName()).build()
                )
        );

        return AysErrorResponse.builder().subErrors(subErrors);
    }

    /**
     * A static method that creates an {@link AysErrorResponseBuilder} instance with the given {@link MethodArgumentTypeMismatchException}
     * as a sub-error.
     *
     * @param exception a {@link MethodArgumentTypeMismatchException} object to be used as a sub-error in the {@link AysErrorResponse} instance
     * @return an instance of {@link AysErrorResponseBuilder} with the given {@link MethodArgumentTypeMismatchException} object as a sub-error
     */
    public static AysErrorResponse.AysErrorResponseBuilder subErrors(final MethodArgumentTypeMismatchException exception) {
        return AysErrorResponse.builder()
                .subErrors(List.of(
                        SubError.builder()
                                .message(exception.getMessage().split(";")[0])
                                .field(exception.getName())
                                .value(Objects.requireNonNull(exception.getValue()).toString())
                                .type(Objects.requireNonNull(exception.getRequiredType()).getSimpleName()).build()
                ));
    }
}
