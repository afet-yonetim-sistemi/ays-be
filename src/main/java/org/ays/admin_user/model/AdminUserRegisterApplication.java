package org.ays.admin_user.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import org.ays.common.model.BaseDomainModel;
import org.ays.institution.model.Institution;

/**
 * Admin User Register Application Domain Model to perform data transfer from service layer to controller
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AdminUserRegisterApplication extends BaseDomainModel {

    private String id;
    private String reason;
    private String rejectReason;
    private AdminUserRegisterApplicationStatus status;

    private AdminUser adminUser;
    private Institution institution;

}
