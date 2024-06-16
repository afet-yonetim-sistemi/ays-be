package org.ays.user.model.entity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.auth.model.enums.AysUserType;
import org.ays.common.model.entity.BaseEntity;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.user.model.enums.UserRole;
import org.ays.user.model.enums.UserStatus;
import org.ays.user.model.enums.UserSupportStatus;
import org.ays.user.model.request.UserUpdateRequest;

import java.util.List;

/**
 * User entity, which holds the information regarding the system user.
 */
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "AYS_USER")
@Deprecated(since = "UserEntity V2 Production'a alınınca burası silinecektir.", forRemoval = true)
public class UserEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "INSTITUTION_ID")
    private String institutionId;

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
    private String countryCode;

    @Column(name = "LINE_NUMBER")
    private String lineNumber;

    @OneToOne
    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private InstitutionEntity institution;

    @Enumerated(EnumType.STRING)
    @Column(name = "SUPPORT_STATUS")
    private UserSupportStatus supportStatus;

    public boolean isActive() {
        return UserStatus.ACTIVE.equals(this.status);
    }

    public boolean isPassive() {
        return UserStatus.PASSIVE.equals(this.status);
    }

    public boolean isDeleted() {
        return UserStatus.DELETED.equals(this.status);
    }

    public boolean isReady() {
        return UserSupportStatus.READY.equals(this.supportStatus);
    }

    public void delete() {
        this.status = UserStatus.DELETED;
    }

    public void ready() {
        this.supportStatus = UserSupportStatus.READY;
    }

    public void busy() {
        this.supportStatus = UserSupportStatus.BUSY;
    }

    public void onRoad() {
        this.supportStatus = UserSupportStatus.ON_ROAD;
    }

    public Claims getClaims() {
        final ClaimsBuilder claimsBuilder = Jwts.claims();
        claimsBuilder.add(AysTokenClaims.USER_ID.getValue(), this.id);
        claimsBuilder.add(AysTokenClaims.USERNAME.getValue(), this.username);
        claimsBuilder.add(AysTokenClaims.USER_TYPE.getValue(), AysUserType.USER);
        claimsBuilder.add(AysTokenClaims.ROLES.getValue(), List.of(this.role));
        claimsBuilder.add(AysTokenClaims.INSTITUTION_ID.getValue(), this.institutionId);
        return claimsBuilder.build();
    }

    public void update(UserUpdateRequest updateRequest) {
        this.role = updateRequest.getRole();
        this.status = updateRequest.getStatus();
    }

    public void updateSupportStatus(UserSupportStatus supportStatus) {
        this.supportStatus = supportStatus;
    }
}
