package org.ays.assignment.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.assignment.model.dto.request.AssignmentSaveRequest;
import org.ays.assignment.model.entity.AssignmentEntity;
import org.ays.assignment.model.mapper.AssignmentSaveRequestToAssignmentMapper;
import org.ays.assignment.repository.AssignmentRepository;
import org.ays.assignment.service.AssignmentSaveService;
import org.ays.auth.model.AysIdentity;
import org.springframework.stereotype.Service;

/**
 * UserSaveServiceImpl is an implementation of the {@link AssignmentSaveService} interface.
 * It provides methods for saving assignment data and performing related operations by admin.
 */
@Service
@RequiredArgsConstructor
class AssignmentSaveServiceImpl implements AssignmentSaveService {

    private final AssignmentRepository assignmentRepository;

    private final AysIdentity identity;

    private static final AssignmentSaveRequestToAssignmentMapper assignmentSaveRequestToAssignmentMapper = AssignmentSaveRequestToAssignmentMapper.initialize();

    /**
     * Saves a new assignment based on the provided save request.
     *
     * @param saveRequest the request object containing assignment data to be saved
     * @return the saved assignment
     */
    @Override
    public void saveAssignment(AssignmentSaveRequest saveRequest) {

        final AssignmentEntity assignmentEntity = assignmentSaveRequestToAssignmentMapper.mapForSaving(saveRequest, identity.getInstitutionId());

        assignmentRepository.save(assignmentEntity);

    }

}
