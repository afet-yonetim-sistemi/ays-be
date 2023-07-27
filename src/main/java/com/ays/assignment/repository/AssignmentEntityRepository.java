package com.ays.assignment.repository;

import com.ays.assignment.model.entity.AssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on UserAssignmentEntity objects.
 */
public interface AssignmentEntityRepository extends JpaRepository<AssignmentEntity, String> {

}
