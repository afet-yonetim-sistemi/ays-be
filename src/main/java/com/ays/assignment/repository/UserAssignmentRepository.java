package com.ays.assignment.repository;

import com.ays.assignment.model.entity.UserAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on UserAssignmentEntity objects.
 */
public interface UserAssignmentRepository extends JpaRepository<UserAssignmentEntity, String> {

}
