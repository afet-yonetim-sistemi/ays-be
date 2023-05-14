package com.ays.common.model.dto.response;

import com.ays.common.util.exception.model.AysError;
import com.ays.common.util.exception.model.AysErrorBuilder;
import org.springframework.http.HttpStatus;

public class AysResponseBuilder {

    public static final AysResponse<Void> SUCCESS = AysResponse.<Void>builder()
            .httpStatus(HttpStatus.OK)
            .isSuccess(true).build();

    public static final AysResponse<AysError> UNAUTHORIZED = AysResponse.<AysError>builder()
            .httpStatus(HttpStatus.UNAUTHORIZED)
            .isSuccess(false)
            .response(AysErrorBuilder.UNAUTHORIZED).build();

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