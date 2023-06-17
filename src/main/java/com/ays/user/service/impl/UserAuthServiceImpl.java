package com.ays.user.service.impl;

import com.ays.auth.model.AysIdentity;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.dto.request.AysLoginRequest;
import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.auth.service.AysInvalidTokenService;
import com.ays.auth.service.AysTokenService;
import com.ays.auth.util.exception.PasswordNotValidException;
import com.ays.auth.util.exception.UserNotActiveException;
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
 * UserAuthServiceImpl is an implementation of the {@link UserAuthService} interface.
 * It provides methods for user authentication and token management.
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
     * Authenticates a user based on the provided login request.
     *
     * @param loginRequest the request object containing login credentials
     * @return an access token for the authenticated user
     * @throws PasswordNotValidException if the provided password is not valid
     */
    @Override
    public AysToken authenticate(AysLoginRequest loginRequest) {

        final UserEntity userEntity = this.findUser(loginRequest.getUsername());

        if (!passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())) {
            throw new PasswordNotValidException();
        }

        return tokenService.generate(userEntity.getClaims());

    }

    /**
     * Generates a new access token based on the provided refresh token.
     *
     * @param refreshToken the refresh token used for generating a new access token
     * @return a new access token
     */
    public AysToken refreshAccessToken(final String refreshToken) {

        tokenService.verifyAndValidate(refreshToken);
        final String username = tokenService
                .getClaims(refreshToken)
                .get(AysTokenClaims.USERNAME.getValue()).toString();

        final UserEntity userEntity = this.findUser(username);

        return tokenService.generate(userEntity.getClaims(), refreshToken);
    }

    /**
     * Finds a user entity based on the provided username.
     *
     * @param username the username of the user to find
     * @return the user entity
     * @throws UsernameNotValidException if a user with the given username is not found
     * @throws UserNotActiveException    if the user is not active
     */
    private UserEntity findUser(String username) {
        final UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotValidException(username));

        if (!userEntity.isActive()) {
            throw new UserNotActiveException(username);
        }

        return userEntity;
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
