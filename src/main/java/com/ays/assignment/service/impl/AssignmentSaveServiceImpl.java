package com.ays.assignment.service.impl;

import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.dto.request.AssignmentSaveRequest;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.mapper.AssignmentSaveRequestToAssignmentMapper;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.service.AssignmentSaveService;
import com.ays.auth.model.AysIdentity;
import com.ays.common.model.AysPhoneNumber;
import com.ays.user.util.exception.AysUserAlreadyExistsByPhoneNumberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Assignment saveAssignment(AssignmentSaveRequest saveRequest) {

        final List<AssignmentEntity> assignmentsFromDatabase = assignmentRepository.findAll();

        this.checkPhoneNumberExist(saveRequest.getPhoneNumber(), assignmentsFromDatabase);

        final AssignmentEntity assignmentEntity = assignmentSaveRequestToAssignmentMapper.mapForSaving(saveRequest, identity.getInstitutionId());

        AssignmentEntity savedAssignment = assignmentRepository.save(assignmentEntity);

        return Assignment.builder()
                .id(savedAssignment.getId())
                .description(saveRequest.getDescription())
                .firstName(saveRequest.getFirstName())
                .lastName(saveRequest.getLastName())
                .latitude(saveRequest.getLatitude())
                .longitude(saveRequest.getLongitude())
                .phoneNumber(saveRequest.getPhoneNumber())
                .build();

    }

    /**
     * Checks if a phone number already exists in the database.
     *
     * @param phoneNumber       the phone number to check
     * @param usersFromDatabase the list of assignment entities from the database
     * @throws AysUserAlreadyExistsByPhoneNumberException if a user with the same phone number already exists
     */
    private void checkPhoneNumberExist(final AysPhoneNumber phoneNumber,
                                       final List<AssignmentEntity> usersFromDatabase) {

        final boolean isPhoneNumberExist = usersFromDatabase.stream()
                .filter(userEntity -> userEntity.getCountryCode().equals(phoneNumber.getCountryCode()))
                .anyMatch(userEntity -> userEntity.getLineNumber().equals(phoneNumber.getLineNumber()));

        if (isPhoneNumberExist) {
            throw new AysUserAlreadyExistsByPhoneNumberException(phoneNumber);
        }
    }
}
