package org.ays.common.util.exception.model;

import org.springframework.http.HttpStatus;

public class AysErrorBuilder {

    public static final AysErrorResponse VALIDATION_ERROR = AysErrorResponse.builder()
            .httpStatus(HttpStatus.BAD_REQUEST)
            .header(AysErrorResponse.Header.VALIDATION_ERROR.getName())
            .isSuccess(false).build();

    public static final AysErrorResponse FORBIDDEN = AysErrorResponse.builder()
            .httpStatus(HttpStatus.FORBIDDEN)
            .header(AysErrorResponse.Header.AUTH_ERROR.getName())
            .isSuccess(false).build();

}
