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

import java.util.EnumSet;

/**
 * Implementation of {@link AssignmentHandler} for {@link AssignmentHandlerType#CANCEL} type
 * This class is responsible for handling the {@link AssignmentEntity} with {@link AssignmentStatus#ASSIGNED} or {@link AssignmentStatus#IN_PROGRESS} status.
 */
@Component
@RequiredArgsConstructor
public class AssignmentCancelHandler implements AssignmentHandler {

    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final AysIdentity identity;

    /**
     * Retrieves the type of this assignment handler.
     *
     * @return The type of this assignment handler, which is {@link AssignmentHandlerType#CANCEL}.
     */
    @Override
    public AssignmentHandlerType type() {
        return AssignmentHandlerType.CANCEL;
    }

    /**
     * Handles the assignment by updating the status and state of the associated entities.
     * <li> Sets the user associated with the assignment to a busy state.
     * <li> Changes the status of the assignment to available.
     */
    @Override
    public void handle() {
        final AssignmentEntity assignmentEntity = this.findAssignmentEntity();
        final UserEntity userEntity = assignmentEntity.getUser();
        userEntity.busy();
        userRepository.save(userEntity);

        assignmentEntity.available();
        assignmentRepository.save(assignmentEntity);
    }

    /**
     * Finds and returns an assignment entity based on the current user's identity, with a status of {@link AssignmentStatus#ASSIGNED} or {@link AssignmentStatus#IN_PROGRESS}.
     *
     * @return The found assignment entity.
     */
    private AssignmentEntity findAssignmentEntity() {
        final String userId = identity.getUserId();
        final EnumSet<AssignmentStatus> acceptedStatuses = EnumSet.of(AssignmentStatus.ASSIGNED, AssignmentStatus.IN_PROGRESS);
        return assignmentRepository.findByUserIdAndStatusIn(userId, acceptedStatuses)
                .orElseThrow(() -> new AysAssignmentNotExistByUserIdAndStatusException(userId, acceptedStatuses));
    }
}
