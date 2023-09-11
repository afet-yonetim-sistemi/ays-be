package com.ays.assignment.service.impl.handler;


import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.enums.AssignmentHandlerType;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.util.exception.AysAssignmentUserNotStatusException;
import com.ays.auth.model.AysIdentity;
import com.ays.user.model.enums.UserSupportStatus;
import org.springframework.stereotype.Component;

@Component
class AssignmentStartHandler extends AssignmentAbstractHandler {

    private final AssignmentRepository assignmentRepository;
    private final AysIdentity identity;

    public AssignmentStartHandler(AssignmentRepository assignmentRepository, AysIdentity aysIdentity) {
        super(assignmentRepository, aysIdentity);
        this.assignmentRepository = assignmentRepository;
        this.identity = aysIdentity;
    }

    @Override
    public AssignmentHandlerType type() {
        return AssignmentHandlerType.COMPLETE;
    }

    @Override
    protected AssignmentEntity handle(AssignmentEntity assignmentEntity) {
        assignmentEntity.updateAssignmentStatus(AssignmentStatus.IN_PROGRESS);
        assignmentEntity.getUser().setSupportStatus(UserSupportStatus.ON_ROAD);
        return assignmentEntity;
    }

    @Override
    protected AssignmentEntity findAssignmentEntity() {
        String userId = identity.getUserId();
        AssignmentStatus assignmentStatus = AssignmentStatus.ASSIGNED;
        return assignmentRepository
                .findByUserIdAndStatus(userId, assignmentStatus)
                .orElseThrow(() -> new AysAssignmentUserNotStatusException(assignmentStatus, userId));
    }

}
