package org.ays.user.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.common.model.entity.BaseEntity;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.user.model.enums.UserStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@SuperBuilder
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

    public void delete() {
        this.status = UserStatus.DELETED;
    }

    public Map<String, Object> getClaims() {
        final Map<String, Object> claims = new HashMap<>();
        claims.put(AysTokenClaims.INSTITUTION_ID.getValue(), this.institutionId);
        claims.put(AysTokenClaims.USER_ID.getValue(), this.id);
        claims.put(AysTokenClaims.USER_FIRST_NAME.getValue(), this.firstName);
        claims.put(AysTokenClaims.USER_LAST_NAME.getValue(), this.lastName);
        claims.put(AysTokenClaims.USER_EMAIL_ADDRESS.getValue(), this.emailAddress);
        claims.put(AysTokenClaims.USER_PERMISSIONS.getValue(), this.getPermissionNames());
        return claims;
    }

    private Set<String> getPermissionNames() {
        return this.roles.stream()
                .map(RoleEntity::getPermissions)
                .flatMap(Set::stream)
                .map(PermissionEntity::getName)
                .collect(Collectors.toSet());
    }


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
        private String id;

        @Column(name = "USER_ID")
        private String userId;

        @Column(name = "VALUE")
        private String value;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        private UserEntityV2 user;

    }


}
