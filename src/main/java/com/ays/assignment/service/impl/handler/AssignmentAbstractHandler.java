package com.ays.assignment.service.impl.handler;

import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.util.exception.AysAssignmentNotExistByUserIdAndStatusException;
import com.ays.auth.model.AysIdentity;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
abstract class AssignmentAbstractHandler implements AssignmentHandler {

    private final AssignmentRepository assignmentRepository;
    private final AysIdentity identity;


    /**
     * Run assignment action by given assignment action name.
     *
     * @throws AysAssignmentNotExistByUserIdAndStatusException if current user does not have assignment by status.
     */
    @Override
    @Transactional
    public void handle() {
        AssignmentEntity assignmentEntity = this.findAssignmentEntity();
        AssignmentEntity assignmentEntityToBeSaved = this.handle(assignmentEntity);
        assignmentRepository.save(assignmentEntityToBeSaved);
    }

    /**
     * Abstract method to handle assignment entity.
     *
     * @param assignmentEntity The Assignment Entity to be handled
     * @return Assignment Entity to be saved
     */
    protected abstract AssignmentEntity handle(AssignmentEntity assignmentEntity);

    /**
     * Abstract method to get assignment search status.
     *
     * @return assignment status
     */
    protected abstract AssignmentStatus getAssignmentSearchStatus();

    /**
     * Find assignment entity by current user id and status.
     *
     * @return assignment entity
     * @throws AysAssignmentNotExistByUserIdAndStatusException if current user does not have assignment by status
     */
    private AssignmentEntity findAssignmentEntity() {
        String userId = identity.getUserId();
        AssignmentStatus assignmentStatus = this.getAssignmentSearchStatus();
        return assignmentRepository
                .findByUserIdAndStatus(userId, assignmentStatus)
                .orElseThrow(() -> new AysAssignmentNotExistByUserIdAndStatusException(userId, assignmentStatus));
    }

}
