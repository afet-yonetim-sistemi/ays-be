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
public class AssignmentApproveHandler extends AssignmentAbstractHandler {

    public AssignmentApproveHandler(AssignmentRepository assignmentRepository, AysIdentity aysIdentity) {
        super(assignmentRepository, aysIdentity);
    }

    @Override
    public AssignmentHandlerType type() {
        return AssignmentHandlerType.APPROVE;
    }


    @Override
    protected AssignmentEntity handle(AssignmentEntity assignment) {
        assignment.updateAssignmentStatus(AssignmentStatus.ASSIGNED);
        assignment.getUser().setSupportStatus(UserSupportStatus.BUSY);
        return assignment;
    }

    @Override
    protected AssignmentEntity findAssignmentEntity() {
        return this.findAssignmentByStatuses(List.of(AssignmentStatus.RESERVED));
    }


}
