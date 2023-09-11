package com.ays.assignment.service.impl.handler;


import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.enums.AssignmentHandlerType;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.util.exception.AysAssignmentUserNotReservedException;
import com.ays.auth.model.AysIdentity;
import com.ays.user.model.enums.UserSupportStatus;
import org.springframework.stereotype.Component;

@Component
class AssignmentApproveHandler extends AssignmentAbstractHandler {

    private final AssignmentRepository assignmentRepository;

    public AssignmentApproveHandler(AssignmentRepository assignmentRepository, AysIdentity aysIdentity) {
        super(assignmentRepository, aysIdentity);
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public AssignmentHandlerType type() {
        return AssignmentHandlerType.APPROVE;
    }

    @Override
    protected AssignmentEntity handle(AssignmentEntity assignmentEntity) {
        assignmentEntity.updateAssignmentStatus(AssignmentStatus.ASSIGNED);
        assignmentEntity.getUser().setSupportStatus(UserSupportStatus.BUSY);
        return assignmentEntity;
    }

    @Override
    protected AssignmentEntity findAssignmentEntity(String userId) {
        return assignmentRepository
                .findByUserIdAndStatus(userId, AssignmentStatus.ASSIGNED)
                .orElseThrow(() -> new AysAssignmentUserNotReservedException(userId));
    }
}
