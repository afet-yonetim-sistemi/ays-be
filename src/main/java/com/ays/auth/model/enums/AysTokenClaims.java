package com.ays.auth.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AysTokenClaims {

    JWT_ID("jti"),
    TYPE("type"),
    SUBJECT("sub"),
    ROLES("roles"),
    USERNAME("username"),
    USER_TYPE("userType"),
    USER_FIRST_NAME("userFirstName"),
    USER_LAST_NAME("userLastName"),
    ISSUED_AT("iat"),
    EXPIRES_AT("exp"),
    ALGORITHM("alg");

    private final String value;

}
