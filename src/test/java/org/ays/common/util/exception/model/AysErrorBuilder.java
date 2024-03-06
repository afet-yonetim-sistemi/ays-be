package org.ays.common.util.exception.model;

import org.springframework.http.HttpStatus;

public class AysErrorBuilder {

    public static final AysError VALIDATION_ERROR = AysError.builder()
            .httpStatus(HttpStatus.BAD_REQUEST)
            .header(AysError.Header.VALIDATION_ERROR.getName())
            .isSuccess(false).build();

    public static final AysError FORBIDDEN = AysError.builder()
            .httpStatus(HttpStatus.FORBIDDEN)
            .header(AysError.Header.AUTH_ERROR.getName())
            .isSuccess(false).build();

}
