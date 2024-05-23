package org.ays.landing.repository;

import org.ays.landing.model.entity.EmergencyEvacuationEntity;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository interface for performing CRUD operations on {@link EmergencyEvacuationEntity} in the database.
 */
public interface EmergencyEvacuationApplicationRepository extends JpaRepository<EmergencyEvacuationEntity, Long> {
}
