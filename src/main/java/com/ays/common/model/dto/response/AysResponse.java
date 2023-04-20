package com.ays.common.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class AysResponse<T> {

    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();
    private HttpStatus httpStatus;
    private Boolean isSuccess;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T response;


    public static final AysResponse<Void> SUCCESS = AysResponse.<Void>builder()
            .httpStatus(HttpStatus.OK)
            .isSuccess(true).build();

    public static final AysResponse<Void> NO_CONTENT = AysResponse.<Void>builder()
            .httpStatus(HttpStatus.NO_CONTENT)
            .isSuccess(false).build();

    public static <T> AysResponse<T> successOf(final T response) {
        return AysResponse.<T>builder()
                .httpStatus(HttpStatus.OK)
                .isSuccess(true)
                .response(response).build();
    }

    public static <T> AysResponse<T> createdOf(final T response) {
        return AysResponse.<T>builder()
                .httpStatus(HttpStatus.CREATED)
                .isSuccess(true)
                .response(response).build();
    }

    public static <T> AysResponse<T> failOf(final T response) {
        return AysResponse.<T>builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .isSuccess(false)
                .response(response).build();
    }
}
