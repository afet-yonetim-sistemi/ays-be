package com.ays.assignment.repository;

import com.ays.assignment.model.entity.AssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on UserAssignmentEntity objects.
 */
public interface AssignmentRepository extends JpaRepository<AssignmentEntity, String>, JpaSpecificationExecutor<AssignmentEntity> {

    /**
     * Retrieves an optional AssignmentEntity based on the provided user ID and institution ID.
     *
     * @param id            The ID of the assignment to retrieve.
     * @param institutionId The ID of the institution associated with the assignment.
     * @return An optional AssignmentEntity that matches the specified ID and institution ID, or an empty optional if not found.
     */
    Optional<AssignmentEntity> findByIdAndInstitutionId(String id, String institutionId);

}
