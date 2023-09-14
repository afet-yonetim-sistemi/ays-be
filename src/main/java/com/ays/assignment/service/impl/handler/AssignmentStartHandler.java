package com.ays.assignment.service.impl.handler;


import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.enums.AssignmentHandlerType;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.auth.model.AysIdentity;
import com.ays.user.model.enums.UserSupportStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssignmentStartHandler extends AssignmentAbstractHandler {

    public AssignmentStartHandler(AssignmentRepository assignmentRepository, AysIdentity aysIdentity) {
        super(assignmentRepository, aysIdentity);
    }

    @Override
    public AssignmentHandlerType type() {
        return AssignmentHandlerType.START;
    }

    @Override
    protected AssignmentEntity handle(AssignmentEntity assignmentEntity) {
        assignmentEntity.updateAssignmentStatus(AssignmentStatus.IN_PROGRESS);
        assignmentEntity.getUser().setSupportStatus(UserSupportStatus.ON_ROAD);
        return assignmentEntity;
    }

    @Override
    protected AssignmentEntity findAssignmentEntity() {
        return this.findAssignmentByStatuses(List.of(AssignmentStatus.ASSIGNED));

    }

}
