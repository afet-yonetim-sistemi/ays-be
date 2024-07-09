package org.ays.emergency_application.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.ays.common.util.validation.EnumValidation;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationStatus;
import org.hibernate.validator.constraints.Range;

import java.util.Set;

// TODO : Javadoc
@Getter
public class EmergencyEvacuationApplicationUpdateRequest {

    @NotNull
    @Range(min = 1, max = 999)
    private Integer seatingCount;

    @NotNull
    private Boolean hasObstaclePersonExist; //Engel durumu olan biri var mı?

    @NotNull
    private EmergencyEvacuationApplicationStatus status;

    @Size(max = 1000)
    private String notes;

    @JsonIgnore
    @AssertTrue
    public boolean isStatusValid() {
        return EnumValidation.anyOf(
                status,
                Set.of(EmergencyEvacuationApplicationStatus.PENDING,
                        EmergencyEvacuationApplicationStatus.IN_REVIEW,
                        EmergencyEvacuationApplicationStatus.IN_PROGRESS,
                        EmergencyEvacuationApplicationStatus.RECEIVED_FIRST_APPROVE,
                        EmergencyEvacuationApplicationStatus.RECEIVED_SECOND_APPROVE,
                        EmergencyEvacuationApplicationStatus.RECEIVED_THIRD_APPROVE,
                        EmergencyEvacuationApplicationStatus.COMPLETED,
                        EmergencyEvacuationApplicationStatus.CANCELLED));
    }

}
