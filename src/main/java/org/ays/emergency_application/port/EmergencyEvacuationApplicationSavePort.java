package org.ays.emergency_application.port;

import org.ays.emergency_application.model.EmergencyEvacuationApplication;

/**
 * Save port interface for handling operations related to saving emergency evacuation applications.
 * <p>
 * This interface provides a method to save new or existing emergency evacuation applications.
 * It abstracts the save functionality to allow for various implementations that handle the persistence
 * of emergency evacuation applications.
 * </p>
 */
public interface EmergencyEvacuationApplicationSavePort {

    /**
     * Saves a new or existing emergency evacuation application.
     * <p>
     * This method persists the provided {@link EmergencyEvacuationApplication} instance, either by creating a new
     * record or updating an existing one, depending on the implementation.
     * </p>
     *
     * @param emergencyEvacuationApplication the emergency evacuation application to save
     * @return the saved {@link EmergencyEvacuationApplication}
     */
    EmergencyEvacuationApplication save(EmergencyEvacuationApplication emergencyEvacuationApplication);

}
