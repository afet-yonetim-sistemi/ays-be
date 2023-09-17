package com.ays.admin_user.model.entity;

import com.ays.admin_user.model.enums.AdminUserRegisterVerificationStatus;
import com.ays.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Admin Users Verification entity, which holds the verification information regarding the system user.
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "AYS_ADMIN_USER_REGISTER_VERIFICATION")
public class AdminUserRegisterVerificationEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "REASON")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private AdminUserRegisterVerificationStatus status;

    @Column(name = "ADMIN_USER_ID")
    private String adminUserId;

    @OneToOne
    @JoinColumn(name = "ADMIN_USER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private AdminUserEntity adminUser;

    public boolean isWaiting() {
        return AdminUserRegisterVerificationStatus.WAITING.equals(this.status);
    }

    public void complete(final String adminUserId) {
        this.adminUserId = adminUserId;
        this.status = AdminUserRegisterVerificationStatus.COMPLETED;
    }
}
