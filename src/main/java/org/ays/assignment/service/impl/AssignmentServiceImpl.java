package org.ays.assignment.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.assignment.model.Assignment;
import org.ays.assignment.model.dto.request.AssignmentListRequest;
import org.ays.assignment.model.dto.request.AssignmentUpdateRequest;
import org.ays.assignment.model.entity.AssignmentEntity;
import org.ays.assignment.model.enums.AssignmentStatus;
import org.ays.assignment.model.mapper.AssignmentEntityToAssignmentMapper;
import org.ays.assignment.model.mapper.AssignmentUpdateRequestToAssignmentEntityMapper;
import org.ays.assignment.repository.AssignmentRepository;
import org.ays.assignment.service.AssignmentService;
import org.ays.assignment.util.exception.AysAssignmentNotExistByIdException;
import org.ays.assignment.util.exception.AysAssignmentNotExistByUserIdAndStatusException;
import org.ays.assignment.util.exception.AysAssignmentNotExistByUserIdException;
import org.ays.auth.model.AysIdentity;
import org.ays.common.model.AysPage;
import org.ays.location.model.UserLocation;
import org.ays.location.model.entity.UserLocationEntity;
import org.ays.location.model.mapper.UserLocationEntityToUserLocationMapper;
import org.ays.location.repository.UserLocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final UserLocationRepository userLocationRepository;

    private final AysIdentity identity;

    private static final AssignmentEntityToAssignmentMapper assignmentEntityToAssignmentMapper = AssignmentEntityToAssignmentMapper.initialize();

    private static final AssignmentUpdateRequestToAssignmentEntityMapper assignmentUpdateRequestToAssignmentEntityMapper = AssignmentUpdateRequestToAssignmentEntityMapper.initialize();

    private static final UserLocationEntityToUserLocationMapper userLocationEntityToUserLocationMapper = UserLocationEntityToUserLocationMapper.initialize();


    /**
     * Retrieves the assignment that is assigned to the current user.
     *
     * @return the Assignment object representing the retrieved user's assignment
     */
    public Assignment getUserAssignment() {
        String userId = identity.getUserId();
        final AssignmentEntity assignmentEntity = assignmentRepository
                .findByUserIdAndStatusNot(userId, AssignmentStatus.DONE)
                .filter(assignment -> assignment.isAssigned() || assignment.isInProgress())
                .orElseThrow(() -> new AysAssignmentNotExistByUserIdException(userId));

        return assignmentEntityToAssignmentMapper.map(assignmentEntity);
    }

    /**
     * Retrieves an assignment by their ID.
     *
     * @param id the ID of the assignment to retrieve
     * @return the Assignment object representing the retrieved user
     * @throws AysAssignmentNotExistByIdException if the assignment with the specified ID does not exist
     */
    @Override
    public Assignment getAssignmentById(String id) {

        final AssignmentEntity assignmentEntity = assignmentRepository.findByIdAndInstitutionId(id, identity.getInstitutionId())
                .orElseThrow(() -> new AysAssignmentNotExistByIdException(id));

        Assignment assignment = assignmentEntityToAssignmentMapper.map(assignmentEntity);

        Optional<UserLocationEntity> optionalUserLocationEntity = userLocationRepository
                .findByUserId(assignmentEntity.getUserId());

        if (optionalUserLocationEntity.isPresent()) {

            UserLocation userLocation = userLocationEntityToUserLocationMapper
                    .map(optionalUserLocationEntity.get());

            assignment.getUser().setLocation(userLocation.getPoint());
        }

        return assignment;
    }

    /**
     * Retrieves a paginated list of assignments based on the specified filters and the institution's identity.
     *
     * @param listRequest The request containing filters and pagination parameters.
     * @return An {@link AysPage} object containing the filtered assignments.
     */
    @Override
    public AysPage<Assignment> getAssignments(AssignmentListRequest listRequest) {

        String institutionId = identity.getInstitutionId();

        Specification<AssignmentEntity> byStatusAndPhoneNumber = listRequest.toSpecification(AssignmentEntity.class);
        Specification<AssignmentEntity> byInstitutionId = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("institutionId"), institutionId);

        Page<AssignmentEntity> assignmentEntities = assignmentRepository
                .findAll(byStatusAndPhoneNumber.and(byInstitutionId), listRequest.toPageable());

        List<Assignment> assignments = assignmentEntityToAssignmentMapper.map(assignmentEntities.getContent());

        return AysPage.of(
                listRequest.getFilter(),
                assignmentEntities,
                assignments
        );
    }

    /**
     * <p>Retrieves an assignment of a user if there's one with the statuses:</p>
     * <li>RESERVED</li>
     * <li>ASSIGNED</li>
     * <li>IN_PROGRESS</li>
     * <p>If there's no assignment with the above statuses, the method throws {@link AysAssignmentNotExistByIdException}.</p>
     *
     * @return Assignment
     */
    @Override
    public Assignment getAssignmentSummary() {

        final String userId = identity.getUserId();

        final EnumSet<AssignmentStatus> acceptedStatuses = EnumSet.of(
                AssignmentStatus.ASSIGNED, AssignmentStatus.RESERVED, AssignmentStatus.IN_PROGRESS
        );

        final AssignmentEntity assignmentEntity = assignmentRepository.findByUserIdAndStatusIn(userId, acceptedStatuses)
                .orElseThrow(() -> new AysAssignmentNotExistByUserIdAndStatusException(userId, acceptedStatuses));
        return assignmentEntityToAssignmentMapper.map(assignmentEntity);
    }

    /**
     * Updates an assignment with the provided ID and request.
     *
     * @param id            The ID of the assignment to be updated.
     * @param updateRequest The request containing the new assignment information.
     * @throws AysAssignmentNotExistByIdException if the {@link Assignment} with the specified
     *                                            assignment id and institution id does not exist.
     */
    @Override
    public void updateAssignment(final String id, final AssignmentUpdateRequest updateRequest) {

        AssignmentEntity assignmentEntity = assignmentRepository
                .findByIdAndInstitutionId(id, identity.getInstitutionId())
                .filter(AssignmentEntity::isAvailable)
                .orElseThrow(() -> new AysAssignmentNotExistByIdException(id));

        assignmentEntity.update(assignmentUpdateRequestToAssignmentEntityMapper.map(updateRequest));
        assignmentRepository.save(assignmentEntity);
    }

    /**
     * Deletes an assignment by Assignment ID.
     *
     * @param id The unique identifier of the assignment.
     */
    @Override
    public void deleteAssignment(final String id) {

        AssignmentEntity assignmentEntity = assignmentRepository
                .findByIdAndInstitutionId(id, identity.getInstitutionId())
                .filter(AssignmentEntity::isAvailable)
                .orElseThrow(() -> new AysAssignmentNotExistByIdException(id));

        assignmentRepository.delete(assignmentEntity);
    }

}
