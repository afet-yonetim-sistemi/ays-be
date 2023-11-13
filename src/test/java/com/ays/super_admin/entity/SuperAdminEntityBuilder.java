package com.ays.super_admin.entity;

import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.enums.AdminRole;
import com.ays.admin_user.model.enums.AdminUserStatus;
import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.AysPhoneNumberBuilder;
import com.ays.common.model.TestDataBuilder;
import com.ays.common.util.AysRandomUtil;


public class SuperAdminEntityBuilder extends TestDataBuilder<AdminUserEntity> {

    public SuperAdminEntityBuilder() {
        super(AdminUserEntity.class);
    }

    public SuperAdminEntityBuilder withValidFields() {
        return this
                .withId(AysRandomUtil.generateUUID())
                .withUsername(String.valueOf(AysRandomUtil.generateNumber(6)))
                .withPassword("$2a$10$16pFBczPxydfiRS4whgKfOCxq58L.bB6.i2abkZKR4fpNleQ4SmDy")
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidFields().build())
                .withStatus(AdminUserStatus.ACTIVE)
                .withInstitutionId(null)
                .withRole();
    }

    public SuperAdminEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public SuperAdminEntityBuilder withUsername(String username) {
        data.setUsername(username);
        return this;
    }

    public SuperAdminEntityBuilder withPassword(String password) {
        data.setPassword(password);
        return this;
    }

    public SuperAdminEntityBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setCountryCode(phoneNumber.getCountryCode());
        data.setLineNumber(phoneNumber.getLineNumber());
        return this;
    }

    public SuperAdminEntityBuilder withStatus(AdminUserStatus status) {
        data.setStatus(status);
        return this;
    }

    public SuperAdminEntityBuilder withRole() {
        data.setRole(AdminRole.SUPER_ADMIN);
        return this;
    }

    public SuperAdminEntityBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        return this;
    }

}

