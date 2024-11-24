package org.ays.emergency_application.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.util.validation.NoTrailingOrLeadingSpaces;
import org.ays.emergency_application.model.enums.EmergencyEvacuationApplicationStatus;
import org.hibernate.validator.constraints.Range;

/**
 * A request object for updating an Emergency Evacuation Application.
 * <p>
 * This class contains the details necessary for updating an existing
 * Emergency Evacuation Application.
 * </p>
 */
@Getter
@Setter
public class EmergencyEvacuationApplicationUpdateRequest {

    /**
     * The number of seats available.
     * Must be between 1 and 999.
     */
    @NotNull
    @Range(min = 1, max = 999)
    private Integer seatingCount;

    /**
     * Indicates if there is a person with obstacles present.
     */
    @NotNull
    private Boolean hasObstaclePersonExist;

    /**
     * The current status of the Emergency Evacuation Application.
     */
    @NotNull
    private EmergencyEvacuationApplicationStatus status;

    /**
     * Additional notes regarding the application.
     * The notes can have a maximum length of 1000 characters.
     */
    @Size(max = 1000)
    @NoTrailingOrLeadingSpaces
    private String notes;

}
