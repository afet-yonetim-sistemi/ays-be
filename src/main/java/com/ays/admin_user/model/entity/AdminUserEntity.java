package com.ays.admin_user.model.entity;

import com.ays.admin_user.model.enums.AdminUserStatus;
import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.auth.model.enums.AysUserType;
import com.ays.common.model.entity.BaseEntity;
import com.ays.organization.model.entity.OrganizationEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Admin Users entity, which holds the information regarding the system user.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AYS_ADMIN_USER")
public class AdminUserEntity extends BaseEntity {

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

    @Column(unique = true)
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "STATUS")
    @Column(length = 20, nullable = false)
    private AdminUserStatus status;

    @Column(nullable = false)
    private Integer countryCode;

    @Column(nullable = false)
    private Integer lineNumber;

    @Column(name = "ORGANIZATION_ID")
    private String organizationId;

    @OneToOne
    @JoinColumn(name = "ORGANIZATION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private OrganizationEntity organization;

    public boolean isActive() {
        return AdminUserStatus.ACTIVE.equals(this.status);
    }

    public boolean isNotVerified() {
        return AdminUserStatus.NOT_VERIFIED.equals(this.status);
    }

    public Map<String, Object> getClaims() {
        final Map<String, Object> claims = new HashMap<>();
        claims.put(AysTokenClaims.USERNAME.getValue(), this.username);
        claims.put(AysTokenClaims.USER_TYPE.getValue(), AysUserType.ADMIN);
        claims.put(AysTokenClaims.USER_FIRST_NAME.getValue(), this.firstName);
        claims.put(AysTokenClaims.USER_LAST_NAME.getValue(), this.lastName);
        return claims;
    }

}
