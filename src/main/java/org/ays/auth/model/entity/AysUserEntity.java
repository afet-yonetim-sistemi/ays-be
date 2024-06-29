package org.ays.auth.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.auth.model.enums.AysUserStatus;
import org.ays.common.model.entity.BaseEntity;
import org.ays.institution.model.entity.InstitutionEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a user entity in the application, extending from {@link BaseEntity}.
 * This entity class maps to the database table "AYS_USER".
 */
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "AYS_USER")
public class AysUserEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
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
    private AysUserStatus status;

    @OneToOne(
            mappedBy = "user",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private PasswordEntity password;

    @OneToOne(
            mappedBy = "user",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private LoginAttemptEntity loginAttempt;

    @OneToOne
    @JoinColumn(name = "INSTITUTION_ID", insertable = false, updatable = false)
    private InstitutionEntity institution;

    @ManyToMany
    @JoinTable(
            name = "AYS_USER_ROLE_RELATION",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private List<AysRoleEntity> roles;


    /**
     * Nested entity representing the password information of the user.
     * This entity is mapped to the database table "AYS_USER_PASSWORD".
     */
    @Entity
    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @Table(name = "AYS_USER_PASSWORD")
    public static class PasswordEntity extends BaseEntity {

        @Id
        @Column(name = "ID")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "VALUE")
        private String value;

        @OneToOne
        @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
        private AysUserEntity user;

    }


    /**
     * Nested entity representing the login attempt information of the user.
     * This entity is mapped to the database table "AYS_USER_LOGIN_ATTEMPT".
     */
    @Entity
    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @Table(name = "AYS_USER_LOGIN_ATTEMPT")
    public static class LoginAttemptEntity extends BaseEntity {

        @Id
        @Column(name = "ID")
        @GeneratedValue(strategy = GenerationType.UUID)
        private String id;

        @Column(name = "LAST_LOGIN_AT")
        private LocalDateTime lastLoginAt;

        @OneToOne
        @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
        private AysUserEntity user;

    }

}
