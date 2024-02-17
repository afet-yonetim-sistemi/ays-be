package org.ays.assignment.model.entity;

import org.ays.assignment.model.enums.AssignmentStatus;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.AysPhoneNumberBuilder;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;
import org.ays.location.util.AysLocationUtil;
import org.ays.user.model.entity.UserEntity;
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
