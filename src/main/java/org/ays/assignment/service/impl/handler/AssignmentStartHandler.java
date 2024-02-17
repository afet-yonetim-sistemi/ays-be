package org.ays.assignment.service.impl.handler;


import lombok.RequiredArgsConstructor;
import org.ays.assignment.model.entity.AssignmentEntity;
import org.ays.assignment.model.enums.AssignmentHandlerType;
import org.ays.assignment.model.enums.AssignmentStatus;
import org.ays.assignment.repository.AssignmentRepository;
import org.ays.assignment.util.exception.AysAssignmentNotExistByUserIdAndStatusException;
import org.ays.auth.model.AysIdentity;
import org.ays.user.model.entity.UserEntity;
import org.ays.user.repository.UserRepository;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link AssignmentHandler} for {@link AssignmentHandlerType#START} type.
 * This class is responsible for handling the {@link AssignmentEntity} with {@link AssignmentStatus#ASSIGNED} status.
 */
@Component
@RequiredArgsConstructor
class AssignmentStartHandler implements AssignmentHandler {

    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private final AysIdentity identity;

    /**
     * Retrieves the type of this assignment handler.
     *
     * @return The type of this assignment handler, which is {@link AssignmentHandlerType#START}.
     */
    @Override
    public AssignmentHandlerType type() {
        return AssignmentHandlerType.START;
    }

    /**
     * Handles the assignment by updating the status and state of the associated entities.
     * <li> Sets the user associated with the assignment to an on road state.
     * <li> Changes the status of the assignment to in progress.
     */
    @Override
    public void handle() {
        AssignmentEntity assignmentEntity = this.findAssignmentEntity();
        UserEntity userEntity = assignmentEntity.getUser();
        userEntity.onRoad();
        userRepository.save(userEntity);

        assignmentEntity.start();
        assignmentRepository.save(assignmentEntity);
    }

    /**
     * Finds and returns an assignment entity based on the current user's identity, with a status of {@link AssignmentStatus#ASSIGNED}.
     *
     * @return The found assignment entity.
     * @throws AysAssignmentNotExistByUserIdAndStatusException if no assignment is found for the given user and status.
     */
    private AssignmentEntity findAssignmentEntity() {
        String userId = identity.getUserId();
        AssignmentStatus assignmentStatus = AssignmentStatus.ASSIGNED;
        return assignmentRepository
                .findByUserIdAndStatus(userId, assignmentStatus)
                .orElseThrow(() -> new AysAssignmentNotExistByUserIdAndStatusException(userId, assignmentStatus));
    }
}
