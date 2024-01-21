package com.ays.assignment.repository;

import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.enums.AssignmentStatus;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on UserAssignmentEntity objects.
 */
public interface AssignmentRepository extends JpaRepository<AssignmentEntity, String>, JpaSpecificationExecutor<AssignmentEntity> {

    /**
     * Retrieves a list of {@link AssignmentEntity} instances with a specific status and created before a given timestamp.
     *
     * @param status    The {@link AssignmentStatus} to filter assignments by.
     * @param createdAt The timestamp indicating the maximum creation time for assignments to be included.
     * @return A list of {@link AssignmentEntity} instances that match the specified criteria.
     */
    List<AssignmentEntity> findAllByStatusAndCreatedAtBefore(AssignmentStatus status, LocalDateTime createdAt);

    /**
     * Checks whether an assignment exists for a specific user ID and status.
     *
     * @param userId The ID of the user to check the assignment for.
     * @param status The status of the assignment to check.
     * @return {@code true} if an assignment exists for the specified user ID and status, otherwise {@code false}.
     */
    boolean existsByUserIdAndStatus(String userId, AssignmentStatus status);

    /**
     * Retrieves an optional AssignmentEntity based on the provided user ID and status other than specified.
     *
     * @param userId The unique identifier of the user for whom the assignment is being checked.
     * @param status The status of the assignment that should be excluded from consideration.
     * @return {@code true} if an assignment exists for the given user ID with a status other than the specified status,
     * {@code false} otherwise.
     */
    Optional<AssignmentEntity> findByUserIdAndStatusNot(String userId, AssignmentStatus status);

    /**
     * Retrieves an optional AssignmentEntity based on the provided USER ID and statuses
     *
     * @param userId   The unique identifier of the user for whom the assignment is being checked.
     * @param statuses A collection of Assignment Statuses
     * @return An optional AssignmentEntity that matches with the specified user ID and
     * includes at least one of the specified Assignment statuses
     */
    Optional<AssignmentEntity> findByUserIdAndStatusIn(String userId, EnumSet<AssignmentStatus> statuses);

    /**
     * Retrieves an optional AssignmentEntity based on the provided user ID and institution ID.
     *
     * @param id            The ID of the assignment to retrieve.
     * @param institutionId The ID of the institution associated with the user.
     * @return An optional AssignmentEntity that matches the specified ID and institution ID, or an empty optional if not found.
     */
    Optional<AssignmentEntity> findByIdAndInstitutionId(String id, String institutionId);

    /**
     * Retrieves an optional AssignmentEntity based on the provided user ID.
     *
     * @param userId The ID of the user to retrieve the assignment for.
     * @return An optional AssignmentEntity that matches the specified user ID, or an empty optional if not found.
     */
    Optional<AssignmentEntity> findByUserId(String userId);

    /**
     * Retrieves an optional AssignmentEntity based on the provided user ID and status.
     *
     * @param userId The ID of the user to retrieve the assignment for.
     * @param status The status of the assignment to retrieve.
     * @return An optional AssignmentEntity that matches the specified user ID and status, or an empty optional if not found.
     */
    Optional<AssignmentEntity> findByUserIdAndStatus(String userId, AssignmentStatus status);

    /**
     * Retrieves nearest optional AssignmentEntity based on the provided user location point and institution ID.
     *
     * @param point         The point of the assignment to retrieve.
     * @param institutionId The institution ID of the assignment to retrieve.
     * @return An Optional AssignmentEntity that nearest specified point and institution ID or an empty optional if not found.
     */
    @Query("""
            SELECT assignmentEntity
            FROM AssignmentEntity assignmentEntity
            WHERE assignmentEntity.institutionId = :institutionId AND assignmentEntity.status = 'AVAILABLE'
            ORDER BY st_distance(st_geomfromtext(:#{#point.toString()}, 4326), assignmentEntity.point)
            LIMIT 1"""
    )
    Optional<AssignmentEntity> findNearestAvailableAssignment(Point point, String institutionId);

}
