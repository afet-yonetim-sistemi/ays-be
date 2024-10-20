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

    public static final AysErrorResponse ALREADY_EXIST = AysErrorResponse.builder()
            .header(AysErrorResponse.Header.ALREADY_EXIST.getName())
            .isSuccess(false).build();

    public static final AysErrorResponse BAD_REQUEST = AysErrorResponse.builder()
            .header(AysErrorResponse.Header.BAD_REQUEST.getName())
            .isSuccess(false).build();

    public static final AysErrorResponse NOT_FOUND = AysErrorResponse.builder()
            .header(AysErrorResponse.Header.NOT_FOUND.getName())
            .isSuccess(false).build();

}
