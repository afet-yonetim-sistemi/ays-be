package org.ays.emergency_application.model;

import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.AysPhoneNumberBuilder;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.emergency_application.model.enums.EmergencyEvacuationApplicationPriority;
import org.ays.emergency_application.model.enums.EmergencyEvacuationApplicationStatus;
import org.ays.institution.model.Institution;

public class EmergencyEvacuationApplicationBuilder extends TestDataBuilder<EmergencyEvacuationApplication> {

    public EmergencyEvacuationApplicationBuilder() {
        super(EmergencyEvacuationApplication.class);
    }

    public EmergencyEvacuationApplicationBuilder withValidValues() {
        return this
                .withId(AysRandomUtil.generateUUID())
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidValues().build())
                .withPriority(EmergencyEvacuationApplicationPriority.MEDIUM)
                .withoutApplicant();
    }

    public EmergencyEvacuationApplicationBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public EmergencyEvacuationApplicationBuilder withoutId() {
        data.setId(null);
        return this;
    }

    public EmergencyEvacuationApplicationBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public EmergencyEvacuationApplicationBuilder withSeatingCount(Integer seatingCount) {
        data.setSeatingCount(seatingCount);
        return this;
    }

    public EmergencyEvacuationApplicationBuilder withPriority(EmergencyEvacuationApplicationPriority priority) {
        data.setPriority(priority);
        return this;
    }

    public EmergencyEvacuationApplicationBuilder withStatus(EmergencyEvacuationApplicationStatus status) {
        data.setStatus(status);
        return this;
    }

    public EmergencyEvacuationApplicationBuilder withoutApplicant() {
        data.setApplicantFirstName(null);
        data.setApplicantLastName(null);
        data.setApplicantPhoneNumber(null);
        return this;
    }

    public EmergencyEvacuationApplicationBuilder withHasObstaclePersonExist(Boolean hasObstaclePersonExist) {
        data.setHasObstaclePersonExist(hasObstaclePersonExist);
        return this;
    }

    public EmergencyEvacuationApplicationBuilder withoutHasObstaclePersonExist() {
        data.setHasObstaclePersonExist(null);
        return this;
    }

    public EmergencyEvacuationApplicationBuilder withNotes(String notes) {
        data.setNotes(notes);
        return this;
    }

    public EmergencyEvacuationApplicationBuilder withoutNotes() {
        data.setNotes(null);
        return this;
    }

    public EmergencyEvacuationApplicationBuilder withInstitution(Institution institution) {
        data.setInstitution(institution);
        return this;
    }

    public EmergencyEvacuationApplicationBuilder withoutInstitution() {
        data.setInstitution(null);
        return this;
    }

}
