package org.ays.common.model.response;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class AysResponseBuilder {

    public static <T> AysResponse<T> success() {
        return AysResponse.<T>builder()
                .isSuccess(true)
                .build();
    }

    public static <T> AysResponse<T> successOf(final T response) {
        return AysResponse.<T>builder()
                .isSuccess(true)
                .response(response)
                .build();
    }

    public static <T> AysResponse<AysPageResponse<T>> successPage() {
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
