package org.ays.common.model.response;

import lombok.experimental.UtilityClass;

import java.util.List;

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

    public static <T> AysResponse<AysPageResponse<T>> success() {
        return AysResponse.<AysPageResponse<T>>builder()
                .isSuccess(true)
                .build();
    }

    public static <T> AysResponse<List<T>> successList() {
        return AysResponse.<List<T>>builder()
                .isSuccess(true)
                .build();
    }

}
