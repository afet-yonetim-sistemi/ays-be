package org.ays.common.model.response;

public class AysErrorResponseBuilder {

    public static final AysErrorResponse VALIDATION_ERROR = AysErrorResponse.builder()
            .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
            .isSuccess(false).build();

    public static final AysErrorResponse UNAUTHORIZED = AysErrorResponse.builder()
            .header(AysErrorResponse.Header.AUTH_ERROR.getName())
            .isSuccess(false).build();

    public static final AysErrorResponse FORBIDDEN = AysErrorResponse.builder()
            .header(AysErrorResponse.Header.AUTH_ERROR.getName())
            .isSuccess(false).build();

    public static final AysErrorResponse CONFLICT_ERROR = AysErrorResponse.builder()
            .header(AysErrorResponse.Header.CONFLICT_ERROR.getName())
            .isSuccess(false).build();

    public static final AysErrorResponse NOT_EXIST_ERROR = AysErrorResponse.builder()
            .header(AysErrorResponse.Header.NOT_EXIST_ERROR.getName())
            .isSuccess(false).build();

}
