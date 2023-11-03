package com.ays.admin_user.model.entity;

import com.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import com.ays.common.model.entity.BaseEntity;
import com.ays.institution.model.entity.InstitutionEntity;
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
@Table(name = "AYS_ADMIN_USER_REGISTER_APPLICATION")
public class AdminUserRegisterApplicationEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "REASON")
    private String reason;

    @Column(name = "REJECT_REASON")
    private String rejectReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private AdminUserRegisterApplicationStatus status;

    @Column(name = "ADMIN_USER_ID")
    private String adminUserId;

    @Column(name = "INSTITUTION_ID")
    private String institutionId;

    @OneToOne
    @JoinColumn(name = "ADMIN_USER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private AdminUserEntity adminUser;

    @OneToOne
    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private InstitutionEntity institution;

    public boolean isWaiting() {
        return AdminUserRegisterApplicationStatus.WAITING.equals(this.status);
    }

    public void complete(final String adminUserId) {
        this.adminUserId = adminUserId;
        this.status = AdminUserRegisterApplicationStatus.COMPLETED;
    }
}
