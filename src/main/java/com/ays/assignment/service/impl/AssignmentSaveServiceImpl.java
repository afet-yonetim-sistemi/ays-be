package com.ays.assignment.service.impl;

import com.ays.assignment.model.dto.request.AssignmentSaveRequest;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.mapper.AssignmentSaveRequestToAssignmentMapper;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.service.AssignmentSaveService;
import com.ays.auth.model.AysIdentity;
import com.ays.user.util.exception.AysUserAlreadyExistsByPhoneNumberException;
import lombok.RequiredArgsConstructor;
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
     * @throws AysUserAlreadyExistsByPhoneNumberException if a assignment with the same phone number already exists
     */
    @Override
    public void saveAssignment(AssignmentSaveRequest saveRequest) {

        final AssignmentEntity assignmentEntity = assignmentSaveRequestToAssignmentMapper.mapForSaving(saveRequest, identity.getInstitutionId());

        assignmentEntity.setPoint(saveRequest.getLatitude(), saveRequest.getLongitude());

        assignmentRepository.save(assignmentEntity);

    }

}
