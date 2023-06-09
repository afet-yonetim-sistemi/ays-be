package com.ays.admin_user.service.impl;

import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.mapper.AdminUserRegisterRequestToAdminUserEntityMapper;
import com.ays.admin_user.repository.AdminUserRegisterVerificationRepository;
import com.ays.admin_user.repository.AdminUserRepository;
import com.ays.admin_user.service.AdminUserAuthService;
import com.ays.auth.model.AysIdentity;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.dto.request.AysLoginRequest;
import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.auth.service.AysInvalidTokenService;
import com.ays.auth.service.AysTokenService;
import com.ays.auth.util.exception.PasswordNotValidException;
import com.ays.auth.util.exception.UserNotActiveException;
import com.ays.auth.util.exception.UserNotVerifiedException;
import com.ays.auth.util.exception.UsernameNotValidException;
import com.ays.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * This service class implements the {@link AdminUserAuthService} interface and provides methods for
 * registering and authenticating admin users. It uses the {@link AdminUserRepository} and
 * {@link AdminUserRegisterVerificationRepository} for persistence operations and the
 * {@link AdminUserRegisterRequestToAdminUserEntityMapper} for mapping the request to entity objects.
 * It also uses the {@link OrganizationRepository} to check if the requested organization exists.
 * Authentication is handled using the {@link PasswordEncoder} and the {@link AysTokenService} is used for
 * generating and refreshing access tokens.
 */
@Service
@RequiredArgsConstructor
@Transactional
class AdminUserAuthServiceImpl implements AdminUserAuthService {

    private final AdminUserRepository adminUserRepository;
    private final AysTokenService tokenService;
    private final AysInvalidTokenService invalidTokenService;
    private final PasswordEncoder passwordEncoder;

    private final AysIdentity identity;

    /**
     * Authenticates an admin user based on the given {@link AysLoginRequest} object. First, it retrieves the
     * {@link AdminUserEntity} associated with the provided username. Then, it checks if the user is active and
     * verified, and if the provided password matches the one stored in the database. If successful, an access
     * token is generated using the {@link AysTokenService} and returned.
     *
     * @param loginRequest the request object containing the username and password for authentication
     * @return an access and refresh tokens for the authenticated admin user
     * @throws UsernameNotFoundException if an admin user with the provided username is not found
     * @throws UserNotVerifiedException  if the admin user is not verified
     * @throws UserNotActiveException    if the admin user is not active
     * @throws PasswordNotValidException if the provided password is not valid
     */
    @Override
    public AysToken authenticate(final AysLoginRequest loginRequest) {

        final AdminUserEntity adminUserEntity = this.findUser(loginRequest.getUsername());

        if (!passwordEncoder.matches(loginRequest.getPassword(), adminUserEntity.getPassword())) {
            throw new PasswordNotValidException();
        }

        return tokenService.generate(adminUserEntity.getClaims());
    }

    /**
     * Refreshes an access token based on the provided refresh token. First, it verifies and validates the
     * refresh token using the {@link AysTokenService}. Then, it retrieves the {@link AdminUserEntity} associated
     * with the username stored in the refresh token's claims. If successful, a new access token is generated
     * using the {@link AysTokenService} and returned.
     *
     * @param refreshToken the refresh token used for generating a new access token
     * @return a new access token for the authenticated admin user
     * @throws UsernameNotFoundException if an admin user with the provided username is not found
     * @throws UserNotVerifiedException  if the admin user is not verified
     * @throws UserNotActiveException    if the admin user is not active
     * @throws PasswordNotValidException if the provided password is not valid
     */
    @Override
    public AysToken refreshAccessToken(final String refreshToken) {

        tokenService.verifyAndValidate(refreshToken);
        final String username = tokenService
                .getClaims(refreshToken)
                .get(AysTokenClaims.USERNAME.getValue()).toString();

        final AdminUserEntity adminUserEntity = this.findUser(username);

        return tokenService.generate(adminUserEntity.getClaims(), refreshToken);
    }

    private AdminUserEntity findUser(String username) {
        final AdminUserEntity adminUserEntity = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotValidException(username));

        if (!adminUserEntity.isActive()) {

            if (adminUserEntity.isNotVerified()) {
                throw new UserNotVerifiedException(username);
            }

            throw new UserNotActiveException(username);
        }

        return adminUserEntity;
    }


    /**
     * Invalidates the access token and refresh token associated with the specified refresh token.
     * It verifies and validates the refresh token first before proceeding with invalidation.
     * If either the access token or refresh token is already marked as invalid, a TokenAlreadyInvalidatedException is thrown.
     *
     * @param refreshToken the refresh token used to invalidate the associated access token and refresh token
     */
    @Override
    public void invalidateTokens(final String refreshToken) {

        tokenService.verifyAndValidate(refreshToken);
        final String refreshTokenId = tokenService.getClaims(refreshToken)
                .get(AysTokenClaims.JWT_ID.getValue()).toString();
        invalidTokenService.checkForInvalidityOfToken(refreshTokenId);

        final String accessTokenId = tokenService.getClaims(identity.getAccessToken())
                .get(AysTokenClaims.JWT_ID.getValue()).toString();
        invalidTokenService.invalidateTokens(Set.of(accessTokenId, refreshTokenId));
    }

}
