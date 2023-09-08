package com.ays.assignment.service.impl;

import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.dto.request.AssignmentUpdateRequest;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.mapper.AssignmentEntityToAssignmentMapper;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.service.AssignmentService;
import com.ays.assignment.util.exception.AysAssignmentNotExistByIdException;
import com.ays.auth.model.AysIdentity;
import com.ays.location.model.UserLocation;
import com.ays.location.model.entity.UserLocationEntity;
import com.ays.location.model.mapper.UserLocationEntityToUserLocationMapper;
import com.ays.location.repository.UserLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final UserLocationRepository userLocationRepository;

    private final AysIdentity identity;

    private static final AssignmentEntityToAssignmentMapper assignmentEntityToAssignmentMapper = AssignmentEntityToAssignmentMapper.initialize();
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
     * Updates an assignment with the provided ID and request.
     *
     * @param id The ID of the assignment to be updated.
     * @param updateRequest The request containing the new assignment information.
     * @throws AysAssignmentNotExistByIdException if the {@link Assignment} with the specified
     * assignment id and institution id does not exist.
     */
    @Override
    public void updateAssignment(String id, AssignmentUpdateRequest updateRequest) {

        AssignmentEntity assignmentEntity = assignmentRepository
                .findByIdAndInstitutionId(id, identity.getInstitutionId())
                .filter(AssignmentEntity::isAvailable)
                .orElseThrow(() -> new AysAssignmentNotExistByIdException(id));

        assignmentEntity.updateAssignment(updateRequest);
        assignmentRepository.save(assignmentEntity);
    }

}
