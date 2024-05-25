package org.ays.emergency_application.repository;

import org.ays.emergency_application.model.entity.EmergencyEvacuationEntity;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository interface for performing CRUD operations on {@link EmergencyEvacuationEntity} in the database.
 */
public interface EmergencyEvacuationApplicationRepository extends JpaRepository<EmergencyEvacuationEntity, String> {
}
