package com.ays.assignment.service.impl.handler;


import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.enums.AssignmentHandlerType;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.util.exception.AysAssignmentNotExistByUserIdAndStatusException;
import com.ays.auth.model.AysIdentity;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link AssignmentAbstractHandler} for {@link AssignmentHandlerType#APPROVE} type
 * This class is responsible for handling the {@link AssignmentEntity} with {@link AssignmentStatus#RESERVED} status.
 */
@Component
@RequiredArgsConstructor
class AssignmentApproveHandler implements AssignmentHandler {

    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private final AysIdentity identity;

    @Override
    public AssignmentHandlerType type() {
        return AssignmentHandlerType.APPROVE;
    }

    @Override
    public void handle() {
        AssignmentEntity assignmentEntity = this.findAssignmentEntity();
        UserEntity userEntity = assignmentEntity.getUser();
        userEntity.busy();
        userRepository.save(userEntity);

        assignmentEntity.assign();
        assignmentRepository.save(assignmentEntity);
    }

    private AssignmentEntity findAssignmentEntity() {
        String userId = identity.getUserId();
        AssignmentStatus assignmentStatus = AssignmentStatus.RESERVED;
        return assignmentRepository
                .findByUserIdAndStatus(userId, assignmentStatus)
                .orElseThrow(() -> new AysAssignmentNotExistByUserIdAndStatusException(userId, assignmentStatus));
    }
}
