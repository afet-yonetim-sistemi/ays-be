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
public class AssignmentCompleteHandler extends AssignmentAbstractHandler {


    public AssignmentCompleteHandler(AssignmentRepository assignmentRepository, AysIdentity identity) {
        super(assignmentRepository, identity);
    }

    @Override
    public AssignmentHandlerType type() {
        return AssignmentHandlerType.COMPLETE;
    }

    @Override
    protected AssignmentEntity handle(AssignmentEntity assignmentEntity) {
        assignmentEntity.updateAssignmentStatus(AssignmentStatus.DONE);
        assignmentEntity.getUser().setSupportStatus(UserSupportStatus.READY);
        return assignmentEntity;
    }

    @Override
    protected AssignmentEntity findAssignmentEntity() {
        return this.findAssignmentByStatuses(List.of(AssignmentStatus.IN_PROGRESS));
    }

}
