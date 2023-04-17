package com.ays.user.model.entity;

import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.auth.model.enums.AysUserType;
import com.ays.common.model.entity.BaseEntity;
import com.ays.organization.model.entity.OrganizationEntity;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Users entity, which holds the information regarding the system user.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AYS_USER")
public class UserEntity extends BaseEntity {

    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "role", nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "status")
    @Column(length = 20, nullable = false)
    private UserStatus status;

    @Column(nullable = false)
    private Integer countryCode;

    @Column(nullable = false)
    private Integer lineNumber;

    @Column(name = "organization_id")
    private String organizationId;

    @OneToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "id", insertable = false, updatable = false)
    private OrganizationEntity organization;

    public boolean isActive() {
        return UserStatus.ACTIVE.equals(this.status);
    }

    public void deleteUser() {
        this.status = UserStatus.DELETED;
    }

    public Map<String, Object> getClaims() {
        final Map<String, Object> claims = new HashMap<>();
        claims.put(AysTokenClaims.USERNAME.getValue(), this.username);
        claims.put(AysTokenClaims.USER_TYPE.getValue(), AysUserType.USER);
        claims.put(AysTokenClaims.ROLES.getValue(), List.of(this.role));
        claims.put(AysTokenClaims.USER_FIRST_NAME.getValue(), this.firstName);
        claims.put(AysTokenClaims.USER_LAST_NAME.getValue(), this.lastName);
        return claims;
    }
}
