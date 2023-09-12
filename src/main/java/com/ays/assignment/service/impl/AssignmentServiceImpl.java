package com.ays.assignment.service.impl;

import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.dto.request.AssignmentListRequest;
import com.ays.assignment.model.dto.request.AssignmentUpdateRequest;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.mapper.AssignmentEntityToAssignmentMapper;
import com.ays.assignment.model.mapper.AssignmentUpdateRequestToAssignmentEntityMapper;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.service.AssignmentService;
import com.ays.assignment.util.exception.AysAssignmentNotExistByIdException;
import com.ays.auth.model.AysIdentity;
import com.ays.common.model.AysPage;
import com.ays.location.model.UserLocation;
import com.ays.location.model.entity.UserLocationEntity;
import com.ays.location.model.mapper.UserLocationEntityToUserLocationMapper;
import com.ays.location.repository.UserLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
     * Updates an assignment with the provided ID and request.
     *
     * @param id            The ID of the assignment to be updated.
     * @param updateRequest The request containing the new assignment information.
     * @throws AysAssignmentNotExistByIdException if the {@link Assignment} with the specified
     *                                            assignment id and institution id does not exist.
     */
    @Override
    public void updateAssignment(String id, AssignmentUpdateRequest updateRequest) {

        AssignmentEntity assignmentEntity = assignmentRepository
                .findByIdAndInstitutionId(id, identity.getInstitutionId())
                .filter(AssignmentEntity::isAvailable)
                .orElseThrow(() -> new AysAssignmentNotExistByIdException(id));

        assignmentEntity.update(assignmentUpdateRequestToAssignmentEntityMapper.map(updateRequest));
        assignmentRepository.save(assignmentEntity);
    }

}
