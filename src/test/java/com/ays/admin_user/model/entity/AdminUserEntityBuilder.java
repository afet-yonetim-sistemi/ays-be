package com.ays.admin_user.model.entity;

import com.ays.admin_user.model.enums.AdminUserStatus;
import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.AysPhoneNumberBuilder;
import com.ays.common.model.TestDataBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.organization.model.entity.OrganizationEntity;
import com.ays.organization.model.entity.OrganizationEntityBuilder;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;

import java.util.ArrayList;
import java.util.List;

public class AdminUserEntityBuilder extends TestDataBuilder<AdminUserEntity> {

    public AdminUserEntityBuilder() {
        super(AdminUserEntity.class);
    }

    public static List<AdminUserEntity> generateValidUserEntities(int size) {
        List<AdminUserEntity> userEntities = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            AdminUserEntity userEntity = new AdminUserEntityBuilder().withValidFields().build();
            userEntities.add(userEntity);
        }
        return userEntities;
    }

    public AdminUserEntityBuilder withValidFields() {
        OrganizationEntity organizationEntity = new OrganizationEntityBuilder().withValidFields().build();
        return this
                .withId(AysRandomUtil.generateUUID())
                .withUsername(String.valueOf(AysRandomUtil.generateNumber(6)))
                .withPassword("$2a$10$H/lKEaKsusQztOaJmYTAi.4MAmjvnxWOh0DY.XrgwHy5D2gENVIky")
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidFields().build())
                .withStatus(AdminUserStatus.ACTIVE)
                .withOrganizationId(organizationEntity.getId())
                .withOrganization(organizationEntity);
    }

    public AdminUserEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AdminUserEntityBuilder withUsername(String username) {
        data.setUsername(username);
        return this;
    }

    public AdminUserEntityBuilder withPassword(String password) {
        data.setPassword(password);
        return this;
    }

    public AdminUserEntityBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setCountryCode(phoneNumber.getCountryCode());
        data.setLineNumber(phoneNumber.getLineNumber());
        return this;
    }

    public AdminUserEntityBuilder withStatus(AdminUserStatus status) {
        data.setStatus(status);
        return this;
    }

    public AdminUserEntityBuilder withOrganizationId(String organizationId) {
        data.setOrganizationId(organizationId);
        return this;
    }

    public AdminUserEntityBuilder withOrganization(OrganizationEntity organization) {
        data.setOrganization(organization);
        return this;
    }

}
