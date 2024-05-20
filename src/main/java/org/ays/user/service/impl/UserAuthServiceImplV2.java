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
import org.ays.auth.util.exception.UserDoesNotAccessPageException;
import org.ays.auth.util.exception.UserIdNotValidException;
import org.ays.auth.util.exception.UserNotActiveException;
import org.ays.user.model.entity.RoleEntity;
import org.ays.user.model.entity.UserEntityV2;
import org.ays.user.model.entity.UserLoginAttemptEntity;
import org.ays.user.model.enums.SourcePage;
import org.ays.user.repository.UserLoginAttemptRepository;
import org.ays.user.repository.UserRepositoryV2;
import org.ays.user.service.UserAuthServiceV2;
import org.ays.user.util.exception.AysUserLoginAttemptNotExistException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
@RequiredArgsConstructor
class UserAuthServiceImplV2 implements UserAuthServiceV2 {

    private final UserRepositoryV2 userRepository;
    private final UserLoginAttemptRepository loginAttemptRepository;
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
     * @return AysToken representing the access token generated upon successful authentication.
     * @throws EmailAddressNotValidException  If the provided email address is not valid or does not exist.
     * @throws PasswordNotValidException      If the provided password is not valid.
     * @throws UserNotActiveException         If the user's status is not active.
     * @throws UserDoesNotAccessPageException If the user does not have permission to access the requested page.
     */
    @Override
    public AysToken authenticate(final AysLoginRequestV2 loginRequest) {

        final UserEntityV2 userEntity = userRepository.findByEmailAddress(loginRequest.getEmailAddress())
                .orElseThrow(() -> new EmailAddressNotValidException(loginRequest.getEmailAddress()));

        if (!passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword().getValue())) {
            throw new PasswordNotValidException();
        }

        this.validateUserStatus(userEntity);
        this.validateUserSourcePagePermission(userEntity, loginRequest.getSourcePage());

        final UserLoginAttemptEntity loginAttemptEntity = this.saveLoginAttempt(userEntity);
        final Claims claimsOfUser = userEntity.getClaims(loginAttemptEntity);
        return tokenService.generate(claimsOfUser);
    }

    /**
     * Saves a login attempt for the specified user. If a login attempt already exists for the user,
     * it marks the attempt as successful and updates it in the repository. If no prior login attempt
     * exists, it creates a new one and saves it in the repository.
     *
     * @param userEntity the user for whom the login attempt is being recorded
     * @return the saved or updated {@link UserLoginAttemptEntity} instance
     */
    private UserLoginAttemptEntity saveLoginAttempt(UserEntityV2 userEntity) {

        final Optional<UserLoginAttemptEntity> loginAttemptEntityFromDatabase = loginAttemptRepository
                .findByUserId(userEntity.getId());
        if (loginAttemptEntityFromDatabase.isPresent()) {
            UserLoginAttemptEntity loginAttemptEntity = loginAttemptEntityFromDatabase.get();
            loginAttemptEntity.success();
            loginAttemptRepository.save(loginAttemptEntity);
            return loginAttemptEntity;
        }

        final UserLoginAttemptEntity loginAttemptEntity = UserLoginAttemptEntity.builder()
                .userId(userEntity.getId())
                .build();
        loginAttemptRepository.save(loginAttemptEntity);
        return loginAttemptEntity;
    }

    /**
     * Validates whether the user has permission to access the specified source page.
     *
     * <p>
     * This method checks if the user's roles contain any permissions associated with the specified source page.
     * If the user has permission, it returns true; otherwise, it throws a UserDoesNotAccessPageException.
     *
     * @param userEntity The user entity for which permissions are to be checked.
     * @param sourcePage The source page for which permission is to be validated.
     * @throws UserDoesNotAccessPageException If the user does not have permission to access the specified source page.
     */
    private void validateUserSourcePagePermission(final UserEntityV2 userEntity,
                                                  final SourcePage sourcePage) {

        boolean hasUserPermission = userEntity.getRoles().stream()
                .map(RoleEntity::getPermissions)
                .flatMap(Set::stream)
                .anyMatch(permission -> permission.getName().equals(sourcePage.getPermission()));

        if (!hasUserPermission) {
            throw new UserDoesNotAccessPageException(userEntity.getId(), sourcePage);
        }
    }

    /**
     * Refreshes the access token using the provided refresh token.
     *
     * <p>
     * This method verifies and validates the provided refresh token. If the token is valid,
     * it retrieves the user ID from the token payload and fetches the corresponding user entity.
     * It then validates the user's status and generates a new access token based on the user's
     * claims and the provided refresh token.
     * </p>
     *
     * @param refreshToken The refresh token used to generate a new access token.
     * @return AysToken representing the new access token generated upon successful token refresh.
     * @throws UserIdNotValidException   if an user with the provided userId is not found
     * @throws UserNotActiveException    if the user is not active
     * @throws PasswordNotValidException if the provided password is not valid
     * @throws UserNotActiveException    If the user's status is not active.
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

        this.validateUserStatus(userEntity);

        final UserLoginAttemptEntity loginAttemptEntity = loginAttemptRepository.findByUserId(userId)
                .orElseThrow(() -> new AysUserLoginAttemptNotExistException(userId));

        final Claims claimsOfUser = userEntity.getClaims(loginAttemptEntity);
        return tokenService.generate(claimsOfUser, refreshToken);
    }

    /**
     * Validates the status of the user entity.
     *
     * <p>
     * This method checks if the status of the user entity is active. If the user is not active,
     * it throws a UserNotActiveException.
     *
     * @param userEntity The user entity for which the status is to be validated.
     * @throws UserNotActiveException If the user's status is not active.
     */
    private void validateUserStatus(final UserEntityV2 userEntity) {

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
