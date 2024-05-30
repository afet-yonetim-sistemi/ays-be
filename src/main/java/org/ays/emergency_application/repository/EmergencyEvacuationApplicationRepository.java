package org.ays.emergency_application.repository;

import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * Repository interface for performing CRUD operations on {@link EmergencyEvacuationApplicationEntity} in the database.
 */
public interface EmergencyEvacuationApplicationRepository extends JpaRepository<EmergencyEvacuationApplicationEntity, String>, JpaSpecificationExecutor<EmergencyEvacuationApplicationEntity>{
}
