package com.ays.common.util.exception.model.enums;

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

@Getter
@Builder
public class AysError {

    @Builder.Default
    private LocalDateTime requestTime = LocalDateTime.now();
    private String header;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @Builder.Default
    private final Boolean isSuccess = false;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SubError> subErrors;

    @Getter
    @Builder
    public static class SubError {
        private String message;
        private String field;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Object value;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String type;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Header {
        API_ERROR("API ERROR"),
        ALREADY_EXIST("ALREADY EXIST"),
        NOT_FOUND("NOT EXIST"),
        VALIDATION_ERROR("VALIDATION ERROR"),
        DATABASE_ERROR("DATABASE ERROR"),
        PROCESS_ERROR("PROCESS ERROR");

        private final String name;
    }

    public static AysError.AysErrorBuilder subErrors(final List<FieldError> fieldErrors) {

        if (CollectionUtils.isEmpty(fieldErrors)) {
            return AysError.builder();
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

        return AysError.builder().subErrors(subErrorErrors);
    }

    public static AysError.AysErrorBuilder subErrors(final Set<ConstraintViolation<?>> constraintViolations) {

        if (CollectionUtils.isEmpty(constraintViolations)) {
            return AysError.builder();
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

        return AysError.builder().subErrors(subErrors);
    }

    public static AysError.AysErrorBuilder subErrors(final MethodArgumentTypeMismatchException exception) {
        return AysError.builder()
                .subErrors(List.of(
                        SubError.builder()
                                .message(exception.getMessage().split(";")[0])
                                .field(exception.getName())
                                .value(exception.getValue() != null ? exception.getValue().toString() : null)
                                .type(exception.getRequiredType().getSimpleName()).build()
                ));
    }
}
