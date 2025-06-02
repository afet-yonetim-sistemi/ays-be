package org.ays.auth.model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jwts;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.auth.model.enums.AysUserStatus;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.BaseDomainModel;
import org.ays.institution.model.Institution;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Domain model representing a user entity for data transfer between the service layer and controller.
 * This class extends {@link BaseDomainModel} to inherit common properties and behavior.
 * It encapsulates user-specific information such as identification, contact details, status,
 * authentication credentials, roles, and associated institution.
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AysUser extends BaseDomainModel {

    private String id;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private AysPhoneNumber phoneNumber;
    private String city;
    private AysUserStatus status;

    private Password password;
    private LoginAttempt loginAttempt;

    private List<AysRole> roles;
    private Institution institution;

    /**
     * Checks if the user's status is active.
     *
     * @return {@code true} if the user's status is {@link AysUserStatus#ACTIVE}, otherwise {@code false}.
     */
    public boolean isActive() {
        return this.status == AysUserStatus.ACTIVE;
    }

    /**
     * Checks if the user's status is passive.
     *
     * @return {@code true} if the user's status is {@link AysUserStatus#PASSIVE}, otherwise {@code false}.
     */
    public boolean isPassive() {
        return this.status == AysUserStatus.PASSIVE;
    }

    /**
     * Checks if the user's status is not passive.
     *
     * @return {@code true} if the user's status is not {@link AysUserStatus#PASSIVE}, otherwise {@code false}.
     */
    public boolean isNotPassive() {
        return this.status != AysUserStatus.PASSIVE;
    }

    /**
     * Checks if the user's status is deleted.
     *
     * @return {@code true} if the user's status is {@link AysUserStatus#DELETED}, otherwise {@code false}.
     */
    public boolean isDeleted() {
        return this.status == AysUserStatus.DELETED;
    }


    /**
     * Updates the user information with the specified details.
     * </p>
     *
     * @param emailAddress the email address of the user
     * @param firstName    the first name of the user
     * @param lastName     the last name of the user
     * @param phoneNumber  the {@link AysPhoneNumber} containing the user's phone information
     * @param city         the city where the user is located
     * @param roleIds      the set of role IDs assigned to the user
     * @param updatedUser  the identifier of the user who performed the update
     */
    public void update(final String emailAddress,
                       final String firstName,
                       final String lastName,
                       final AysPhoneNumber phoneNumber,
                       final String city,
                       final Set<String> roleIds,
                       final String updatedUser) {

        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.roles = roleIds.stream()
                .map(roleId -> AysRole.builder().id(roleId).build())
                .collect(Collectors.toList());
        this.updatedUser = updatedUser;
    }

    /**
     * Sets the user's status to {@link AysUserStatus#ACTIVE}, marking the user as active.
     */
    public void activate() {
        this.status = AysUserStatus.ACTIVE;
    }

    /**
     * Sets the user's status to {@link AysUserStatus#PASSIVE}, marking the user as passive.
     */
    public void passivate() {
        this.status = AysUserStatus.PASSIVE;
    }

    /**
     * Sets the user's status to {@link AysUserStatus#DELETED}, marking the user as deleted.
     */
    public void delete() {
        this.status = AysUserStatus.DELETED;
    }

    /**
     * Sets the user's status to {@link AysUserStatus#NOT_VERIFIED}, indicating the user's verification status.
     * This method typically sets the status to indicate the user's account has not yet been verified.
     */
    public void notVerify() {
        this.status = AysUserStatus.NOT_VERIFIED;
    }


    /**
     * Domain model representing user password information.
     * This class encapsulates the unique identifier and encrypted value of a user's password.
     */
    @Getter
    @Setter
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    public static class Password extends BaseDomainModel {

        private String id;
        private String value;
        private LocalDateTime forgotAt;

    }


    /**
     * Domain model representing user login attempt information.
     * This class encapsulates the unique identifier and timestamp of a user's last login attempt.
     * It provides a method to update the last login timestamp to the current time.
     */
    @Getter
    @Setter
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    public static class LoginAttempt extends BaseDomainModel {

        private String id;
        private LocalDateTime lastLoginAt;

        public void success() {
            this.lastLoginAt = LocalDateTime.now();
        }

    }

    /**
     * Generates JWT claims based on the user's information for authentication and authorization.
     * The claims include user-specific data such as institution ID, institution name, user ID, first name,
     * last name, email address, and permissions associated with the user.
     * If available, it also includes the timestamp of the user's last successful login attempt.
     *
     * @return JWT claims containing user-related information.
     */
    public Claims getClaims() {
        final ClaimsBuilder claimsBuilder = Jwts.claims();

        claimsBuilder.add(AysTokenClaims.INSTITUTION_ID.getValue(), this.institution.getId());
        claimsBuilder.add(AysTokenClaims.INSTITUTION_NAME.getValue(), this.institution.getName());
        claimsBuilder.add(AysTokenClaims.USER_ID.getValue(), this.id);
        claimsBuilder.add(AysTokenClaims.USER_FIRST_NAME.getValue(), this.firstName);
        claimsBuilder.add(AysTokenClaims.USER_LAST_NAME.getValue(), this.lastName);
        claimsBuilder.add(AysTokenClaims.USER_EMAIL_ADDRESS.getValue(), this.emailAddress);
        claimsBuilder.add(AysTokenClaims.USER_PERMISSIONS.getValue(), this.getPermissionNames());


        if (this.loginAttempt != null && this.loginAttempt.lastLoginAt != null) {
            claimsBuilder.add(AysTokenClaims.USER_LAST_LOGIN_AT.getValue(), this.loginAttempt.lastLoginAt.toString());
        }

        return claimsBuilder.build();
    }

    private List<String> getPermissionNames() {
        return this.roles.stream()
                .map(AysRole::getPermissions)
                .flatMap(List::stream)
                .map(AysPermission::getName)
                .toList();
    }

}
