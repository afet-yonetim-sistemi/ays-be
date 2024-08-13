package org.ays.emergency_application.model.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.emergency_application.model.enums.EmergencyEvacuationApplicationStatus;

public class EmergencyEvacuationApplicationUpdateRequestBuilder extends TestDataBuilder<EmergencyEvacuationApplicationUpdateRequest> {

    public EmergencyEvacuationApplicationUpdateRequestBuilder() {
        super(EmergencyEvacuationApplicationUpdateRequest.class);
    }

    public EmergencyEvacuationApplicationUpdateRequestBuilder withValidValues() {
        return this
                .withSeatingCount(1)
                .withHasObstaclePersonExist(true)
                .withStatus(EmergencyEvacuationApplicationStatus.IN_REVIEW)
                .withNotes("This is a valid note for the application update.");
    }

    public EmergencyEvacuationApplicationUpdateRequestBuilder withSeatingCount(Integer seatingCount) {
        data.setSeatingCount(seatingCount);
        return this;
    }

    public EmergencyEvacuationApplicationUpdateRequestBuilder withHasObstaclePersonExist(Boolean hasObstaclePersonExist) {
        data.setHasObstaclePersonExist(hasObstaclePersonExist);
        return this;
    }

    public EmergencyEvacuationApplicationUpdateRequestBuilder withStatus(EmergencyEvacuationApplicationStatus status) {
        data.setStatus(status);
        return this;
    }

    public EmergencyEvacuationApplicationUpdateRequestBuilder withNotes(String notes) {
        data.setNotes(notes);
        return this;
    }
}
