package org.ays.common.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.ays.common.util.AysRandomUtil;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A generic response class representing an API response.
 *
 * @param <T> The type of the response object.
 */
@Getter
@Builder
public class AysResponse<T> {

    /**
     * The timestamp indicating when the response was created.
     */
    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();

    /**
     * A unique code identifying the response.
     */
    @Builder.Default
    private String code = AysRandomUtil.generateUUID();

    /**
     * Indicates whether the API request was successful or not.
     */
    private Boolean isSuccess;

    /**
     * The response object.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T response;


    /**
     * A pre-defined success response with no content.
     */
    public static final AysResponse<Void> SUCCESS = AysResponse.<Void>builder()
            .isSuccess(true).build();

    /**
     * Creates a success response with the specified response object.
     *
     * @param <T>      The type of the response object.
     * @param response The response object.
     * @return An instance of {@link AysResponse} representing a successful response.
     */
    public static <T> AysResponse<T> successOf(final T response) {
        return AysResponse.<T>builder()
                .isSuccess(true)
                .response(response).build();
    }

}
