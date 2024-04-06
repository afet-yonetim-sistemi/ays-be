package org.ays.common.util.exception.model;

public class AysErrorBuilder {

    public static final AysErrorResponse VALIDATION_ERROR = AysErrorResponse.builder()
            .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
            .isSuccess(false).build();

    public static final AysErrorResponse FORBIDDEN = AysErrorResponse.builder()
            .header(AysErrorResponse.Header.AUTH_ERROR.getName())
            .isSuccess(false).build();

}
