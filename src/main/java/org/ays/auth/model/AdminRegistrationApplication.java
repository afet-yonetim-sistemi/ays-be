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

    private UserV2 user;
    private Institution institution;

}
