package org.ays.auth.service.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysToken;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.enums.AysSourcePage;
import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.auth.model.request.AysLoginRequest;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.auth.service.AysAuthService;
import org.ays.auth.service.AysInvalidTokenService;
import org.ays.auth.service.AysTokenService;
import org.ays.auth.util.exception.AysEmailAddressNotValidException;
import org.ays.auth.util.exception.AysPasswordNotValidException;
import org.ays.auth.util.exception.AysUserDoesNotAccessPageException;
import org.ays.auth.util.exception.AysUserIdNotValidException;
import org.ays.auth.util.exception.AysUserNotActiveException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Provides authentication services for users.
 *
 * <p>
 * This service class handles user authentication, token generation, and token refresh functionalities.
 *
 * <p>
 * It interacts with user repositories, password encoders, token services, and identity services to authenticate users,
 * generate access tokens, and refresh access tokens securely.
 */
@Service
@RequiredArgsConstructor
class AysAuthServiceImpl implements AysAuthService {

    private final AysUserReadPort userReadPort;
    private final AysUserSavePort userSavePort;
    private final PasswordEncoder passwordEncoder;

    private final AysTokenService tokenService;
    private final AysInvalidTokenService invalidTokenService;

    private final AysIdentity identity;

    /**
     * Authenticates a user based on the provided login request.
     *
     * <p>
     * This method retrieves the user entity associated with the provided email address from the repository.
     * It then verifies the password against the encoded password stored in the database. If the password
     * is valid, it proceeds to validate the user's status and permissions for the requested source page.
     * Upon successful authentication, it updates the user's last login attempt and generates an access token.
     * </p>
     *
     * @param loginRequest The login request containing the user's email address, password, and source page.
     * @return {@link AysToken} representing the access token generated upon successful authentication.
     * @throws AysEmailAddressNotValidException  If the provided email address is not valid or does not exist.
     * @throws AysPasswordNotValidException      If the provided password is not valid.
     * @throws AysUserNotActiveException         If the user's status is not active.
     * @throws AysUserDoesNotAccessPageException If the user does not have permission to access the requested page.
     */
    @Override
    @Transactional
    public AysToken authenticate(final AysLoginRequest loginRequest) {

        final AysUser user = userReadPort.findByEmailAddress(loginRequest.getEmailAddress())
                .orElseThrow(() -> new AysEmailAddressNotValidException(loginRequest.getEmailAddress()));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword().getValue())) {
            throw new AysPasswordNotValidException();
        }

        this.validateUserStatus(user);
        this.validateUserSourcePagePermission(user, loginRequest.getSourcePage());

        Optional.ofNullable(user.getLoginAttempt())
                .ifPresentOrElse(AysUser.LoginAttempt::success,
                        () -> {
                            user.setLoginAttempt(AysUser.LoginAttempt.builder().build());
                            user.getLoginAttempt().success();
                        });
        userSavePort.save(user);

        final Claims claimsOfUser = user.getClaims();
        return tokenService.generate(claimsOfUser);
    }


    /**
     * Validates whether the user has permission to access the specified source page.
     *
     * <p>
     * This method checks if the user's roles contain any permissions associated with the specified source page.
     * If the user has permission, it returns true; otherwise, it throws a {@link AysUserDoesNotAccessPageException}.
     *
     * @param user       The user for which permissions are to be checked.
     * @param sourcePage The source page for which permission is to be validated.
     * @throws AysUserDoesNotAccessPageException If the user does not have permission to access the specified source page.
     */
    private void validateUserSourcePagePermission(final AysUser user,
                                                  final AysSourcePage sourcePage) {

        boolean hasUserPermission = user.getRoles().stream()
                .map(AysRole::getPermissions)
                .flatMap(List::stream)
                .anyMatch(permission -> permission.getName().equals(sourcePage.getPermission()));

        if (!hasUserPermission) {
            throw new AysUserDoesNotAccessPageException(user.getId(), sourcePage);
        }
    }

    /**
     * Refreshes the access token using the provided refresh token.
     * Verifies the refresh token, validates the user's status, and generates a new access token.
     *
     * @param refreshToken The refresh token used to generate a new access token.
     * @return A new {@link AysToken} containing the refreshed access token.
     * @throws AysUserIdNotValidException If the user ID extracted from the refresh token is not valid.
     * @throws AysUserNotActiveException  If the user associated with the refresh token is not active.
     */
    @Override
    @Transactional(readOnly = true)
    public AysToken refreshAccessToken(final String refreshToken) {

        tokenService.verifyAndValidate(refreshToken);

        final Claims claims = tokenService.getPayload(refreshToken);

        final String refreshTokenId = claims.getId();

        invalidTokenService.checkForInvalidityOfToken(refreshTokenId);

        final String userId = claims.get(AysTokenClaims.USER_ID.getValue()).toString();

        final AysUser user = userReadPort.findById(userId)
                .orElseThrow(() -> new AysUserIdNotValidException(userId));

        this.validateUserStatus(user);

        final Claims claimsOfUser = user.getClaims();
        return tokenService.generate(claimsOfUser, refreshToken);
    }

    /**
     * Validates the status of the user.
     * Throws {@link AysUserNotActiveException} if the user is not active.
     *
     * @param user The {@link AysUser} object whose status needs to be validated.
     * @throws AysUserNotActiveException If the user is not active.
     */
    private void validateUserStatus(final AysUser user) {

        if (!user.isActive()) {
            throw new AysUserNotActiveException(user.getId());
        }
    }


    /**
     * Invalidates the access token and refresh token associated with the specified refresh token.
     * It verifies and validates the refresh token first before proceeding with invalidation.
     * If either the access token or refresh token is already marked as invalid, a TokenAlreadyInvalidatedException is thrown.
     *
     * @param refreshToken the refresh token used to invalidate the associated access token and refresh token
     */
    @Override
    @Transactional
    public void invalidateTokens(final String refreshToken) {

        tokenService.verifyAndValidate(refreshToken);
        final String refreshTokenId = tokenService.getPayload(refreshToken).getId();
        invalidTokenService.checkForInvalidityOfToken(refreshTokenId);

        final String accessTokenId = tokenService.getPayload(identity.getAccessToken()).getId();
        invalidTokenService.invalidateTokens(Set.of(accessTokenId, refreshTokenId));
    }

}
