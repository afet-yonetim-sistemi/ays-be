package org.ays.auth.model;

import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.common.model.enums.BeanScope;
import org.ays.common.util.AysListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class provides a representation of the identity of the authenticated user in the AYS service.
 * It manages information about the user's ID, roles, and associated institution based on JWT claims.
 * <p>
 * Designed to be request-scoped, each HTTP request will have its own instance of this class.
 * This ensures that user-specific details are appropriately isolated per request.
 * The proxy mode is set to {@link org.springframework.context.annotation.ScopedProxyMode#TARGET_CLASS}
 * to allow for lazy initialization and dependency injection of request-scoped beans.
 */
@Component
@Scope(value = BeanScope.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AysIdentity {

    /**
     * Returns the user ID associated with the authenticated user's AYS identity.
     * The user ID is extracted from the JWT claims.
     *
     * @return the user ID as a {@link String}
     */
    public String getUserId() {
        return this.getJwt().getClaim(AysTokenClaims.USER_ID.getValue());
    }

    /**
     * Checks if the authenticated user has a super admin role.
     * This is determined by inspecting the user's permissions in the JWT claims.
     *
     * @return {@code true} if the user has the "super" permission, {@code false} otherwise
     */
    public boolean isSuperAdmin() {
        final List<String> permissions = AysListUtil.to(this.getJwt().getClaim(AysTokenClaims.USER_PERMISSIONS.getValue()), String.class);
        return permissions.stream().anyMatch("super"::equals);
    }

    /**
     * Returns the institution ID associated with the authenticated user's AYS identity.
     * The institution ID is extracted from the JWT claims.
     *
     * @return the institution ID as a {@link String}
     */
    public String getInstitutionId() {
        return this.getJwt().getClaim(AysTokenClaims.INSTITUTION_ID.getValue());
    }

    /**
     * Retrieves the access token value used for the current session.
     * This method extracts the token value from the JWT object.
     *
     * @return the access token value as a {@link String}
     */
    public String getAccessToken() {
        return this.getJwt().getTokenValue();
    }

    /**
     * Retrieves the JWT token for the authenticated user from the security context.
     * This method is used internally to access user-specific claims from the JWT.
     *
     * @return the JWT token as a {@link Jwt} object
     */
    private Jwt getJwt() {
        return ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

}
