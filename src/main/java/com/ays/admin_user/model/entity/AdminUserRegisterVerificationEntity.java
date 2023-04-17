package com.ays.admin_user.model.entity;

import com.ays.admin_user.model.enums.AdminUserRegisterVerificationStatus;
import com.ays.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Admin Users Verification entity, which holds the verification information regarding the system user.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AYS_ADMIN_USER_REGISTER_VERIFICATION")
public class AdminUserRegisterVerificationEntity extends BaseEntity {

    @Id
    private String id;

    private String reason;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "STATUS")
    @Column(length = 20, nullable = false)
    private AdminUserRegisterVerificationStatus status;

    @Column(name = "ADMIN_USER_ID")
    private String adminUserId;

    @OneToOne
    @JoinColumn(name = "ADMIN_USER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private AdminUserEntity adminUser;

    public void complete(final String adminUserId) {
        this.adminUserId = adminUserId;
        this.status = AdminUserRegisterVerificationStatus.COMPLETED;
    }
}
