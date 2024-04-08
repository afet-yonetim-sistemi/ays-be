package org.ays.common.model.dto.response;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AysResponseBuilder {

    public static final AysResponse<Void> SUCCESS = AysResponse.<Void>builder()
            .isSuccess(true)
            .build();

    public static <T> AysResponse<T> successOf(final T response) {
        return AysResponse.<T>builder()
                .isSuccess(true)
                .response(response)
                .build();
    }

}
