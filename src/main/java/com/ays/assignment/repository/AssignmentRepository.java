package com.ays.assignment.repository;

import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.enums.AssignmentStatus;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

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

    /**
     * Retrieves nearest optional AssignmentEntity based on the provided user location point and institution ID.
     *
     * @param point         The point of the assignment to retrieve.
     * @param institutionId The institution ID of the assignment to retrieve.
     * @return An Optional AssignmentEntity that nearest specified point and institution ID or an empty optional if not found.
     */
    @Query("""
            SELECT Object(assignmentEntity)
            FROM AssignmentEntity assignmentEntity
            WHERE assignmentEntity.institutionId = :institution_id_param
            ORDER BY ST_DISTANCE(ST_GeomFromText(:#{#point_param.toString()}, 4326), assignmentEntity.point)
            LIMIT 1"""
    )
    Optional<AssignmentEntity> findNearestAssignment(@Param("point_param") Point point, @Param("institution_id_param") String institutionId);

}
