package com.ays.user.model.entity;

import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.auth.model.enums.AysUserType;
import com.ays.common.model.entity.BaseEntity;
import com.ays.institution.model.entity.InstitutionEntity;
import com.ays.user.model.dto.request.UserUpdateRequest;
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
 * User entity, which holds the information regarding the system user.
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
    @Column(name = "ID")
    private String id;

    @Column(name = "ORGANIZATION_ID")
    private String organizationId;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private UserStatus status;

    @Column(name = "COUNTRY_CODE")
    private Long countryCode;

    @Column(name = "LINE_NUMBER")
    private Long lineNumber;

    @OneToOne
    @JoinColumn(name = "ORGANIZATION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private InstitutionEntity organization;

    public boolean isActive() {
        return UserStatus.ACTIVE.equals(this.status);
    }

    public boolean isPassive() {
        return UserStatus.PASSIVE.equals(this.status);
    }

    public boolean isDeleted() {
        return UserStatus.DELETED.equals(this.status);
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
        claims.put(AysTokenClaims.ORGANIZATION_ID.getValue(), this.organizationId);
        return claims;
    }

    public void updateUser(UserUpdateRequest updateRequest) {
        this.role = updateRequest.getRole();
        this.status = updateRequest.getStatus();
    }
}
