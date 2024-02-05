package com.ays.common.model.dto.response;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

@UtilityClass
public class AysResponseBuilder {

    public static final AysResponse<Void> SUCCESS = AysResponse.<Void>builder()
            .httpStatus(HttpStatus.OK)
            .isSuccess(true).build();

    public static <T> AysResponse<T> successOf(final T response) {
        return AysResponse.<T>builder()
                .httpStatus(HttpStatus.OK)
                .isSuccess(true)
                .response(response).build();
    }

}
