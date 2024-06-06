package org.ays.user.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ays.common.model.entity.BaseEntity;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.user.model.enums.AdminRegistrationApplicationStatus;

/**
 * Entity class representing an admin user registration application.
 * <p>
 * This class contains details about the registration application, including the institution ID,
 * user ID, reason for registration, rejection reason, and application status. It also provides
 * methods to check the status of the application and update it accordingly.
 * </p>
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "AYS_ADMIN_REGISTRATION_APPLICATION")
public class AdminRegistrationApplicationEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "INSTITUTION_ID")
    private String institutionId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "REASON")
    private String reason;

    @Column(name = "REJECT_REASON")
    private String rejectReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private AdminRegistrationApplicationStatus status;

    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private UserEntityV2 user;

    @OneToOne
    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private InstitutionEntity institution;

    /**
     * Checks if the registration application status is 'WAITING'.
     *
     * @return true if the status is 'WAITING', false otherwise
     */
    public boolean isWaiting() {
        return AdminRegistrationApplicationStatus.WAITING.equals(this.status);
    }

    /**
     * Checks if the registration application status is 'COMPLETED'.
     *
     * @return true if the status is 'COMPLETED', false otherwise
     */
    public boolean isCompleted() {
        return AdminRegistrationApplicationStatus.COMPLETED.equals(this.status);
    }

    /**
     * Marks the registration application as completed and sets the user ID.
     *
     * @param userId the user ID to set
     */
    public void complete(final String userId) {
        this.userId = userId;
        this.status = AdminRegistrationApplicationStatus.COMPLETED;
    }

    /**
     * Marks the registration application as verified.
     */
    public void verify() {
        this.status = AdminRegistrationApplicationStatus.VERIFIED;
    }

    /**
     * Marks the registration application as waiting.
     */
    public void waiting() {
        this.status = AdminRegistrationApplicationStatus.WAITING;
    }

    /**
     * Marks the registration application as rejected and sets the rejection reason.
     *
     * @param rejectReason the reason for rejection
     */
    public void reject(final String rejectReason) {
        this.rejectReason = rejectReason;
        this.status = AdminRegistrationApplicationStatus.REJECTED;
    }

    /**
     * Checks if the registration application status is 'REJECTED'.
     *
     * @return true if the status is 'REJECTED', false otherwise
     */
    public boolean isRejected() {
        return AdminRegistrationApplicationStatus.REJECTED.equals(this.status);
    }

    /**
     * Checks if the registration application status is 'VERIFIED'.
     *
     * @return true if the status is 'VERIFIED', false otherwise
     */
    public boolean isVerified() {
        return AdminRegistrationApplicationStatus.VERIFIED.equals(this.status);
    }
}
