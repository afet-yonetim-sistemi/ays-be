package com.ays.admin_user.model.entity;

import com.ays.admin_user.model.enums.AdminUserStatus;
import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.auth.model.enums.AysUserType;
import com.ays.common.model.entity.BaseEntity;
import com.ays.institution.model.entity.InstitutionEntity;
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
 * Admin User entity, which holds the information regarding the system user.
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
    private Long countryCode;

    @Column(name = "LINE_NUMBER")
    private Long lineNumber;

    @Column(name = "ORGANIZATION_ID")
    private String institutionId;

    @OneToOne
    @JoinColumn(name = "ORGANIZATION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private InstitutionEntity institution;

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
        claims.put(AysTokenClaims.ORGANIZATION_ID.getValue(), this.institutionId);
        return claims;
    }

}
