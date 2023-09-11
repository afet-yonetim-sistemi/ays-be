package com.ays.assignment.service.impl.handler;

import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.util.exception.AysAssignmentUserNotStatusException;
import com.ays.auth.model.AysIdentity;
import lombok.RequiredArgsConstructor;

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

    protected abstract AssignmentEntity handle(AssignmentEntity assignmentEntity);

    protected AssignmentEntity findAssignmentEntity() {
        String userId = identity.getUserId();
        AssignmentStatus assignmentStatus = AssignmentStatus.RESERVED;
        return assignmentRepository
                .findByUserIdAndStatus(userId, assignmentStatus)
                .orElseThrow(() -> new AysAssignmentUserNotStatusException(assignmentStatus, userId));
    }

}
