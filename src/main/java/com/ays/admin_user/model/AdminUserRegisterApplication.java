package com.ays.admin_user.model;

import com.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import com.ays.common.model.BaseDomainModel;
import com.ays.institution.model.Institution;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
