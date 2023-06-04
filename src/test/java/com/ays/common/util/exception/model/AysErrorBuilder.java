package com.ays.common.util.exception.model;

import org.springframework.http.HttpStatus;

public class AysErrorBuilder {


    public static final AysError UNAUTHORIZED = AysError.builder()
            .httpStatus(HttpStatus.UNAUTHORIZED)
            .header(AysError.Header.AUTH_ERROR.getName())
            .isSuccess(false).build();

}
