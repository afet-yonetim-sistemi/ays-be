package org.ays.user.service.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysToken;
import org.ays.auth.model.dto.request.AysLoginRequestV2;
import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.auth.service.AysInvalidTokenService;
import org.ays.auth.service.AysTokenService;
import org.ays.auth.util.exception.EmailAddressNotValidException;
import org.ays.auth.util.exception.PasswordNotValidException;
import org.ays.auth.util.exception.UserDoesNotAccessException;
import org.ays.auth.util.exception.UserIdNotValidException;
import org.ays.auth.util.exception.UserNotActiveException;
import org.ays.auth.util.exception.UserNotVerifiedException;
import org.ays.user.model.entity.RoleEntity;
import org.ays.user.model.entity.UserEntity;
import org.ays.user.model.entity.UserEntityV2;
import org.ays.user.model.entity.UserLoginAttemptEntity;
import org.ays.user.model.enums.SourcePage;
import org.ays.user.repository.UserLoginAttemptRepository;
import org.ays.user.repository.UserRepositoryV2;
import org.ays.user.service.UserAuthServiceV2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
class UserAuthServiceImplV2 implements UserAuthServiceV2 {

    private final UserRepositoryV2 userRepository;
    private final UserLoginAttemptRepository loginAttemptRepository;
    private final PasswordEncoder passwordEncoder;

    private final AysTokenService tokenService;
    private final AysInvalidTokenService invalidTokenService;

    private final AysIdentity identity;

    @Override
    public AysToken authenticate(final AysLoginRequestV2 loginRequest) {

        final UserEntityV2 userEntity = userRepository.findByEmailAddress(loginRequest.getEmailAddress())
                .orElseThrow(() -> new EmailAddressNotValidException(loginRequest.getEmailAddress()));

        if (!passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword().getValue())) {
            throw new PasswordNotValidException();
        }

        final UserLoginAttemptEntity loginAttemptEntity = loginAttemptRepository.findByUserId(userEntity.getId());

        this.validaUserStatus(userEntity);
        this.validateUserSourcePagePermission(userEntity, loginRequest.getSourcePage());

        loginAttemptEntity.success();
        loginAttemptRepository.save(loginAttemptEntity);

        return tokenService.generate(userEntity.getClaims());
    }

    private void validateUserSourcePagePermission(final UserEntityV2 userEntity,
                                                  final SourcePage sourcePage) {

        boolean hasUserPermission = userEntity.getRoles().stream()
                .map(RoleEntity::getPermissions)
                .flatMap(Set::stream)
                .anyMatch(permission -> permission.getName().equals(sourcePage.getPermission()));

        if (!hasUserPermission) {
            throw new UserDoesNotAccessException(userEntity.getId(), sourcePage);
        }
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

        final Claims claims = tokenService.getPayload(refreshToken);

        final String refreshTokenId = claims.getId();

        invalidTokenService.checkForInvalidityOfToken(refreshTokenId);

        final String userId = claims.get(AysTokenClaims.USER_ID.getValue()).toString();

        final UserEntityV2 userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserIdNotValidException(userId));

        this.validaUserStatus(userEntity);

        return tokenService.generate(userEntity.getClaims(), refreshToken);
    }

    private void validaUserStatus(final UserEntityV2 userEntity) {

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
        final String refreshTokenId = tokenService.getPayload(refreshToken)
                .get(AysTokenClaims.JWT_ID.getValue()).toString();
        invalidTokenService.checkForInvalidityOfToken(refreshTokenId);

        final String accessTokenId = tokenService.getPayload(identity.getAccessToken())
                .get(AysTokenClaims.JWT_ID.getValue()).toString();
        invalidTokenService.invalidateTokens(Set.of(accessTokenId, refreshTokenId));
    }

}
