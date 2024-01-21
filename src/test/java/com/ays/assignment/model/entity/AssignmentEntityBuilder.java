package com.ays.assignment.model.entity;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.AysPhoneNumberBuilder;
import com.ays.common.model.TestDataBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.institution.model.entity.InstitutionEntity;
import com.ays.institution.model.entity.InstitutionEntityBuilder;
import com.ays.location.util.AysLocationUtil;
import com.ays.user.model.entity.UserEntity;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

public class AssignmentEntityBuilder extends TestDataBuilder<AssignmentEntity> {

    public AssignmentEntityBuilder() {
        super(AssignmentEntity.class);
    }

    public AssignmentEntityBuilder withValidFields() {
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();

        return this
                .withId(AysRandomUtil.generateUUID())
                .withInstitution(institutionEntity)
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidFields().build())
                .withStatus(AssignmentStatus.AVAILABLE)
                .withPoint(AysLocationUtil.generatePoint(40.981210, 29.000000));
    }

    public AssignmentEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AssignmentEntityBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setCountryCode(phoneNumber.getCountryCode());
        data.setLineNumber(phoneNumber.getLineNumber());
        return this;
    }

    public AssignmentEntityBuilder withStatus(AssignmentStatus status) {
        data.setStatus(status);
        return this;
    }

    public AssignmentEntityBuilder withPoint(Point point) {
        data.setPoint(point);
        return this;
    }

    public AssignmentEntityBuilder withCreatedAt(LocalDateTime createdAt) {
        data.setCreatedAt(createdAt);
        return this;
    }

    public AssignmentEntityBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        return this;
    }

    public AssignmentEntityBuilder withInstitution(InstitutionEntity institution) {
        data.setInstitution(institution);
        return this;
    }

    public AssignmentEntityBuilder withUserId(String userId) {
        data.setUserId(userId);
        return this;
    }

    public AssignmentEntityBuilder withUser(UserEntity user) {
        data.setUser(user);
        return this;
    }

}
