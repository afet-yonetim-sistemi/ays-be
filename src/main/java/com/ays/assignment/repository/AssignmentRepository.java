package com.ays.assignment.repository;

import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.enums.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository interface for performing CRUD operations on UserAssignmentEntity objects.
 */
public interface AssignmentRepository extends JpaRepository<AssignmentEntity, String>, JpaSpecificationExecutor<AssignmentEntity> {

    /**
     * Checks whether an assignment exists for a specific user ID.
     *
     * @param userId The ID of the user to check the assignment for.
     * @return {@code true} if an assignment exists for the specified user ID, otherwise {@code false}.
     */
    boolean existsByUserIdAndStatus(String userId, AssignmentStatus status);

}
