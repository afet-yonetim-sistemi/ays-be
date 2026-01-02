package org.ays.auth.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * AysTokenClaims enum represents the possible claims that can be included in a JWT token for an AYS system.
 * The enum constants represent the name of the claim and the associated value is stored as a final String field.
 */
@Getter
@RequiredArgsConstructor
public enum AysTokenClaims {

    TYPE("typ"),
    USER_ID("userId"),
    USER_PERMISSIONS("userPermissions"),
    USER_FIRST_NAME("userFirstName"),
    USER_LAST_NAME("userLastName"),
    USER_LAST_LOGIN_AT("userLastLoginAt"),
    INSTITUTION_ID("institutionId"),
    INSTITUTION_NAME("institutionName"),
    ALGORITHM("alg");

    private final String value;

}
