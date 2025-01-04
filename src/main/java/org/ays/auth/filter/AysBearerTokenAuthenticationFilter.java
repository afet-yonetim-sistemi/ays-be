package org.ays.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.ays.auth.service.AysInvalidTokenService;
import org.ays.auth.service.AysTokenService;
import org.ays.common.model.request.AysHttpHeader;
import org.ays.common.model.request.AysHttpServletRequest;
import org.ays.common.model.response.AysHttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter responsible for handling Bearer token-based authentication in incoming HTTP requests.
 * <p>
 * This filter intercepts requests and checks for the presence of a Bearer token in the `Authorization` header.
 * If a valid token is found:
 * <ul>
 *     <li>The token is verified and validated using {@link AysTokenService}.</li>
 *     <li>The token's payload is retrieved, and its validity is checked using {@link AysInvalidTokenService}.</li>
 *     <li>An {@link org.springframework.security.core.Authentication} object is created and added to the {@link SecurityContextHolder} for the current request.</li>
 * </ul>
 * If the token is invalid, the filter does not authenticate the request, and a 401 Unauthorized response may be returned.
 * <p>
 * This filter ensures that only authenticated users can access secured resources in the application.
 * It runs with an order of {@code 3}, which allows it to be executed after other filters with lower order values.
 */
@Order(3)
@Component
@RequiredArgsConstructor
public class AysBearerTokenAuthenticationFilter extends OncePerRequestFilter {

    private final AysTokenService tokenService;
    private final AysInvalidTokenService invalidTokenService;


    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest httpServletRequest,
                                    @NonNull HttpServletResponse httpServletResponse,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final AysHttpServletRequest aysHttpServletRequest = (AysHttpServletRequest) httpServletRequest;
        final AysHttpHeader aysHttpHeader = aysHttpServletRequest.getHeader();

        if (aysHttpHeader.hasBearerToken()) {

            final String token = aysHttpHeader.getBearerToken();

            tokenService.verifyAndValidate(token);

            final String tokenId = tokenService.getPayload(token).getId();
            invalidTokenService.checkForInvalidityOfToken(tokenId);

            final var authentication = tokenService.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        final AysHttpServletResponse aysHttpServletResponse = (AysHttpServletResponse) httpServletResponse;
        filterChain.doFilter(aysHttpServletRequest, aysHttpServletResponse);
    }

}
