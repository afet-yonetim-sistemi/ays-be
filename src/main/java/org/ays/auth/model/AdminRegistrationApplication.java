package org.ays.auth.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.auth.model.enums.AdminRegistrationApplicationStatus;
import org.ays.common.model.BaseDomainModel;
import org.ays.institution.model.Institution;

/**
 * Domain model class representing an admin user registration application.
 * <p>
 * This class contains details about the registration application, including the application ID,
 * reason for registration, rejection reason, and application status. It also holds references
 * to the associated user and institution.
 * </p>
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AdminRegistrationApplication extends BaseDomainModel {

    private String id;
    private String reason;
    private String rejectReason;
    private AdminRegistrationApplicationStatus status;

    private AysUser user;
    private Institution institution;


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
     * Checks if the registration application status is 'REJECTED'.
     *
     * @return true if the status is 'REJECTED', false otherwise
     */
    public boolean isRejected() {
        return AdminRegistrationApplicationStatus.REJECTED.equals(this.status);
    }

    /**
     * Checks if the registration application status is 'APPROVED'.
     *
     * @return true if the status is 'APPROVED', false otherwise
     */
    public boolean isApproved() {
        return AdminRegistrationApplicationStatus.APPROVED.equals(this.status);
    }


    /**
     * Marks the registration application as waiting.
     */
    public void waiting() {
        this.status = AdminRegistrationApplicationStatus.WAITING;
    }

    /**
     * Marks the registration application as completed and sets the user.
     *
     * @param user the user to set
     */
    public void complete(final AysUser user) {
        this.user = user;
        this.status = AdminRegistrationApplicationStatus.COMPLETED;
    }

    /**
     * Marks the registration application as verified.
     */
    public void approve() {
        this.status = AdminRegistrationApplicationStatus.APPROVED;
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

}
