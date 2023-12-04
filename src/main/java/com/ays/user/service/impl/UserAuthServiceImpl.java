package com.ays.user.service.impl;

import com.ays.auth.model.AysIdentity;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.dto.request.AysLoginRequest;
import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.auth.service.AysInvalidTokenService;
import com.ays.auth.service.AysTokenService;
import com.ays.auth.util.exception.PasswordNotValidException;
import com.ays.auth.util.exception.UserIdNotValidException;
import com.ays.auth.util.exception.UserNotActiveException;
import com.ays.auth.util.exception.UserNotVerifiedException;
import com.ays.auth.util.exception.UsernameNotValidException;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.repository.UserRepository;
import com.ays.user.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * This class implements the {@link UserAuthService} interface and provides authentication and token-related operations for users.
 * It is annotated with {@code @Service} to indicate that it is a service component in the application.
 * The class is also annotated with {@code @RequiredArgsConstructor} to automatically generate a constructor based on the declared final fields.
 * The {@code @Transactional} annotation ensures that all the methods in this class are executed within a transactional context.
 */
@Service
@RequiredArgsConstructor
@Transactional
class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;

    private final AysTokenService tokenService;
    private final AysInvalidTokenService invalidTokenService;
    private final PasswordEncoder passwordEncoder;

    private final AysIdentity identity;

    /**
     * Authenticates a user based on the given {@link AysLoginRequest} object. First, it retrieves the
     * {@link UserEntity} associated with the provided username. Then, it checks if the user is active and
     * verified, and if the provided password matches the one stored in the database. If successful, an access
     * token is generated using the {@link AysTokenService} and returned.
     *
     * @param loginRequest the request object containing the username and password for authentication
     * @return an access and refresh tokens for the authenticated user
     * @throws UserIdNotValidException   if an user with the provided userId is not valid
     * @throws UserNotVerifiedException  if the user is not verified
     * @throws UserNotActiveException    if the user is not active
     * @throws PasswordNotValidException if the provided password is not valid
     */
    @Override
    public AysToken authenticate(final AysLoginRequest loginRequest) {

        final UserEntity userEntity = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotValidException(loginRequest.getUsername()));

        if (!passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())) {
            throw new PasswordNotValidException();
        }

        this.validateUserStatus(userEntity);

        return tokenService.generate(userEntity.getClaims());
    }

    /**
     * Refreshes an access token based on the provided refresh token. First, it verifies and validates the
     * refresh token using the {@link AysTokenService}. Then, it retrieves the {@link UserEntity} associated
     * with the username stored in the refresh token's claims. If successful, a new access token is generated
     * using the {@link AysTokenService} and returned.
     *
     * @param refreshToken the refresh token used for generating a new access token
     * @return a new access token for the authenticated user
     * @throws UserIdNotValidException   if an user with the provided userId is not found
     * @throws UserNotVerifiedException  if the user is not verified
     * @throws UserNotActiveException    if the user is not active
     * @throws PasswordNotValidException if the provided password is not valid
     */
    @Override
    public AysToken refreshAccessToken(final String refreshToken) {

        tokenService.verifyAndValidate(refreshToken);
        final String userId = tokenService
                .getClaims(refreshToken)
                .get(AysTokenClaims.USER_ID.getValue()).toString();

        final UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserIdNotValidException(userId));

        this.validateUserStatus(userEntity);

        return tokenService.generate(userEntity.getClaims(), refreshToken);
    }

    private void validateUserStatus(final UserEntity userEntity) {
        if (!userEntity.isActive()) {
            throw new UserNotActiveException(userEntity.getId());
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
