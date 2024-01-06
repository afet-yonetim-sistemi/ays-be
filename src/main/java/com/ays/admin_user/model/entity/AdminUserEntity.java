package com.ays.admin_user.model.entity;

import com.ays.admin_user.model.enums.AdminRole;
import com.ays.admin_user.model.enums.AdminUserStatus;
import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.auth.model.enums.AysUserType;
import com.ays.common.model.entity.BaseEntity;
import com.ays.institution.model.entity.InstitutionEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Admin User entity, which holds the information regarding the system user.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "AYS_ADMIN_USER")
public class AdminUserEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Email
    @Column(name = "EMAIL")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private AdminUserStatus status;

    @Column(name = "COUNTRY_CODE")
    private String countryCode;

    @Column(name = "LINE_NUMBER")
    private String lineNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private AdminRole role;

    @Column(name = "INSTITUTION_ID")
    private String institutionId;

    @OneToOne
    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private InstitutionEntity institution;

    public boolean isActive() {
        return AdminUserStatus.ACTIVE.equals(this.status);
    }

    public boolean isNotVerified() {
        return AdminUserStatus.NOT_VERIFIED.equals(this.status);
    }

    public boolean isSuperAdmin() {
        return this.institutionId == null;
    }

    public void activate() {
        this.status = AdminUserStatus.ACTIVE;
    }

    public void passivate() {
        this.status = AdminUserStatus.PASSIVE;
    }

    public Map<String, Object> getClaims() {
        final Map<String, Object> claims = new HashMap<>();

        if (this.isSuperAdmin()) {
            claims.put(AysTokenClaims.USER_TYPE.getValue(), AysUserType.SUPER_ADMIN);
        } else {
            claims.put(AysTokenClaims.USER_TYPE.getValue(), AysUserType.ADMIN);
            claims.put(AysTokenClaims.INSTITUTION_ID.getValue(), this.institutionId);
            claims.put(AysTokenClaims.INSTITUTION_NAME.getValue(), this.institution.getName());
        }

        claims.put(AysTokenClaims.USER_ID.getValue(), this.id);
        claims.put(AysTokenClaims.USERNAME.getValue(), this.username);
        claims.put(AysTokenClaims.USER_FIRST_NAME.getValue(), this.firstName);
        claims.put(AysTokenClaims.USER_LAST_NAME.getValue(), this.lastName);
        return claims;
    }

}
