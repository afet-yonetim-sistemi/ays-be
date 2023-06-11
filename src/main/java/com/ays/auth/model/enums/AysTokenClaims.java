package com.ays.auth.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * AysTokenClaims enum represents the possible claims that can be included in a JWT token for an AYS system.
 * The enum constants represent the name of the claim and the associated value is stored as a final String field.
 */
@Getter
@RequiredArgsConstructor
public enum AysTokenClaims {

    JWT_ID("jti"),
    TYPE("typ"),
    SUBJECT("sub"),
    ROLES("roles"),
    USERNAME("username"),
    USER_TYPE("userType"),
    USER_FIRST_NAME("userFirstName"),
    USER_LAST_NAME("userLastName"),
    INSTITUTION_ID("institutionId"),
    ISSUED_AT("iat"),
    EXPIRES_AT("exp"),
    ALGORITHM("alg"),
    USER_ID("userId");

    private final String value;

}
