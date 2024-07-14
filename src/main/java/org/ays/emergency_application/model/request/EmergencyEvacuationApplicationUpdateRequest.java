package org.ays.emergency_application.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationStatus;
import org.hibernate.validator.constraints.Range;

// TODO : Javadoc
@Getter
@Setter
public class EmergencyEvacuationApplicationUpdateRequest {

    @NotNull
    @Range(min = 1, max = 999)
    private Integer seatingCount;

    @NotNull
    private Boolean hasObstaclePersonExist;

    @NotNull
    private EmergencyEvacuationApplicationStatus status;

    @Size(max = 1000)
    private String notes;

}
