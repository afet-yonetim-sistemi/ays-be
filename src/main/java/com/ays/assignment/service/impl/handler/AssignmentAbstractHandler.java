package com.ays.assignment.service.impl.handler;

import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.util.exception.AysAssignmentUserNotStatusException;
import com.ays.auth.model.AysIdentity;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
abstract class AssignmentAbstractHandler implements AssignmentHandler {

    private final AssignmentRepository assignmentRepository;
    private final AysIdentity identity;


    /**
     * Run assignment action by given assignment action name.
     *
     * @throws AysAssignmentUserNotStatusException if current user does not have reserved assignment
     */
    @Override
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
     * Abstract method to find assignment entity.
     *
     * @return assignment entity
     */
    protected abstract AssignmentEntity findAssignmentEntity();


    /**
     * Find assignment entity by current user id and status.
     *
     * @param assignmentStatuses status list
     * @return assignment entity
     * @throws AysAssignmentUserNotStatusException if current user does not have reserved assignment
     */
    protected AssignmentEntity findAssignmentByStatuses(List<AssignmentStatus> assignmentStatuses) {
        String userId = identity.getUserId();
        return assignmentRepository
                .findByUserIdAndStatusIsIn(userId, assignmentStatuses)
                .orElseThrow(() -> new AysAssignmentUserNotStatusException(userId, assignmentStatuses));
    }

}
