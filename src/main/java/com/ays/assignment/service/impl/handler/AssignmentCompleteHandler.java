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
 * Implementation of {@link AssignmentHandler} for {@link AssignmentHandlerType#COMPLETE} type.
 * This class is responsible for handling the {@link AssignmentEntity} with {@link AssignmentStatus#IN_PROGRESS} status.
 */
@Component
@RequiredArgsConstructor
class AssignmentCompleteHandler implements AssignmentHandler {

    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private final AysIdentity identity;

    /**
     * Retrieves the type of this assignment handler.
     *
     * @return The type of this assignment handler, which is {@link AssignmentHandlerType#COMPLETE}.
     */
    @Override
    public AssignmentHandlerType type() {
        return AssignmentHandlerType.COMPLETE;
    }


    /**
     * Handles the assignment by updating the status and state of the associated entities.
     * <li> Sets the user associated with the assignment to a ready state.
     * <li> Changes the status of the assignment to done.
     */
    @Override
    public void handle() {
        AssignmentEntity assignmentEntity = this.findAssignmentEntity();
        UserEntity user = assignmentEntity.getUser();
        user.ready();
        userRepository.save(user);

        assignmentEntity.done();
        assignmentRepository.save(assignmentEntity);
    }

    /**
     * Finds and returns an assignment entity based on the current user's identity, with a status of {@link AssignmentStatus#IN_PROGRESS}.
     *
     * @return The found assignment entity.
     * @throws AysAssignmentNotExistByUserIdAndStatusException if no assignment is found for the given user and status.
     */
    private AssignmentEntity findAssignmentEntity() {
        String userId = identity.getUserId();
        AssignmentStatus assignmentStatus = AssignmentStatus.IN_PROGRESS;
        return assignmentRepository
                .findByUserIdAndStatus(userId, assignmentStatus)
                .orElseThrow(() -> new AysAssignmentNotExistByUserIdAndStatusException(userId, assignmentStatus));
    }
}
