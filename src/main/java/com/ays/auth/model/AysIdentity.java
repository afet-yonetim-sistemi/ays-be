package com.ays.auth.model;

import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.common.model.enums.BeanScope;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class provides a representation of the identity of the authenticated user in the AYS service.
 * The class is designed to be request-scoped and proxy-target-class scoped, meaning that each HTTP request will have its own instance of this class.
 */
@Component
@Scope(value = BeanScope.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequiredArgsConstructor
public class AysIdentity {

    /**
     * Returns the organization ID associated with the authenticated user's AYS identity.
     *
     * @return the organization ID as a string
     */
    public String getOrganizationId() {
        return this.getJwt().getClaim(AysTokenClaims.ORGANIZATION_ID.getValue());
    }

    /**
     * Retrieves the JWT token for the authenticated user from the security context.
     *
     * @return the JWT token as a Jwt object
     */
    private Jwt getJwt() {
        return ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }


    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public List<String> getUsersType() {
        Authentication authentication = getAuthentication();
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }

}
