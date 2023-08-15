package com.ays.assignment.model.entity;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.AysPhoneNumberBuilder;
import com.ays.common.model.TestDataBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.institution.model.entity.InstitutionEntity;
import com.ays.institution.model.entity.InstitutionEntityBuilder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

import java.util.ArrayList;
import java.util.List;

public class AssignmentEntityBuilder extends TestDataBuilder<AssignmentEntity> {

    public AssignmentEntityBuilder() {
        super(AssignmentEntity.class);
    }

    public static List<AssignmentEntity> generateValidAssignmentEntities(int size) {
        List<AssignmentEntity> userEntities = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            AssignmentEntity assignmentEntity = new AssignmentEntityBuilder().withValidFields().build();
            userEntities.add(assignmentEntity);
        }
        return userEntities;
    }

    public AssignmentEntityBuilder withValidFields() {
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder().withValidFields().build();

        Point point = generatePoint();

        return this
                .withId(AysRandomUtil.generateUUID())
                .withInstitutionId(institutionEntity.getId())
                .withInstitution(institutionEntity)
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidFields().build())
                .withDescription("Description")
                .withFirstName("First Name")
                .withLastName("Last Name")
                .withStatus(AssignmentStatus.AVAILABLE)
                .withPoint(point);
    }

    private Point generatePoint() {
        double x = Math.random();
        double y = Math.random();
        Coordinate[] coordinates = new Coordinate[]{new Coordinate(x, y)};
        CoordinateSequence coordinateSequence = new CoordinateArraySequence(coordinates);
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(coordinateSequence);
        return point;
    }

    public AssignmentEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AssignmentEntityBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        return this;
    }

    public AssignmentEntityBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setCountryCode(phoneNumber.getCountryCode());
        data.setLineNumber(phoneNumber.getLineNumber());
        return this;
    }

    public AssignmentEntityBuilder withInstitution(InstitutionEntity institution) {
        data.setInstitution(institution);
        return this;
    }

    public AssignmentEntityBuilder withDescription(String description) {
        data.setDescription(description);
        return this;
    }

    public AssignmentEntityBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public AssignmentEntityBuilder withLastName(String lastName) {
        data.setLastName(lastName);
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

}
