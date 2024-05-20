package org.ays.user.model.entity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.common.model.entity.BaseEntity;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.user.model.enums.UserStatus;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents user data in the "AYS_USER_V2" table.
 * Includes fields for user details such as ID, email, name, etc.
 * Provides methods for checking user status and generating JWT claims.
 * Manages user passwords via the embedded PasswordEntity class.
 * Utilizes JPA annotations for database mapping.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "AYS_USER_V2")
public class UserEntityV2 extends BaseEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "INSTITUTION_ID")
    private String institutionId;

    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "COUNTRY_CODE")
    private String countryCode;

    @Column(name = "LINE_NUMBER")
    private String lineNumber;

    @Column(name = "CITY")
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private UserStatus status;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private PasswordEntity password;

    @OneToOne
    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private InstitutionEntity institution;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "AYS_USER_ROLE_RELATION",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private Set<RoleEntity> roles;


    public boolean isActive() {
        return UserStatus.ACTIVE.equals(this.status);
    }

    public boolean isPassive() {
        return UserStatus.PASSIVE.equals(this.status);
    }

    public boolean isDeleted() {
        return UserStatus.DELETED.equals(this.status);
    }


    public void activate() {
        this.status = UserStatus.ACTIVE;
    }

    public void passivate() {
        this.status = UserStatus.PASSIVE;
    }

    public void delete() {
        this.status = UserStatus.DELETED;
    }

    /**
     * Generates JWT (JSON Web Token) claims for the user based on their data and login attempt information.
     *
     * @param loginAttemptEntity The UserLoginAttemptEntity object containing login attempt information.
     * @return Claims object containing JWT claims including institution ID, institution name, user ID, user first name,
     * user last name, user email address, user permissions, and optionally user last login time if available.
     */
    public Claims getClaims(final UserLoginAttemptEntity loginAttemptEntity) {
        final ClaimsBuilder claimsBuilder = Jwts.claims();

        claimsBuilder.add(AysTokenClaims.INSTITUTION_ID.getValue(), this.institution.getId());
        claimsBuilder.add(AysTokenClaims.INSTITUTION_NAME.getValue(), this.institution.getName());
        claimsBuilder.add(AysTokenClaims.USER_ID.getValue(), this.id);
        claimsBuilder.add(AysTokenClaims.USER_FIRST_NAME.getValue(), this.firstName);
        claimsBuilder.add(AysTokenClaims.USER_LAST_NAME.getValue(), this.lastName);
        claimsBuilder.add(AysTokenClaims.USER_EMAIL_ADDRESS.getValue(), this.emailAddress);
        claimsBuilder.add(AysTokenClaims.USER_PERMISSIONS.getValue(), this.getPermissionNames());

        if (loginAttemptEntity.getLastLoginAt() != null) {
            claimsBuilder.add(AysTokenClaims.USER_LAST_LOGIN_AT.getValue(), loginAttemptEntity.getLastLoginAt().toString());
        }

        return claimsBuilder.build();
    }

    private Set<String> getPermissionNames() {
        return this.roles.stream()
                .map(RoleEntity::getPermissions)
                .flatMap(Set::stream)
                .map(PermissionEntity::getName)
                .collect(Collectors.toSet());
    }

    /**
     * Represents user passwords stored in the "AYS_USER_PASSWORD" table.
     * Includes fields for password ID, associated user ID, and encrypted password value.
     * Establishes a lazy-loaded one-to-one relationship with UserEntityV2.
     * Facilitates secure management of user passwords.
     */
    @Entity
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @Table(name = "AYS_USER_PASSWORD")
    public static class PasswordEntity extends BaseEntity {

        @Id
        @Column(name = "ID")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "USER_ID")
        private String userId;

        @Column(name = "VALUE")
        private String value;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "USER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
        private UserEntityV2 user;

    }

}
