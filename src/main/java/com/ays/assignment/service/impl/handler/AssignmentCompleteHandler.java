package com.ays.assignment.service.impl.handler;


import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.enums.AssignmentHandlerType;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.auth.model.AysIdentity;
import com.ays.user.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
class AssignmentCompleteHandler extends AssignmentAbstractHandler {

    public AssignmentCompleteHandler(AssignmentRepository assignmentRepository, AysIdentity identity) {
        super(assignmentRepository, identity);
    }

    @Override
    public AssignmentHandlerType type() {
        return AssignmentHandlerType.COMPLETE;
    }


    @Override
    protected AssignmentEntity handle(AssignmentEntity assignment) {
        UserEntity user = assignment.getUser();
        user.ready();

        assignment.done();
        return assignment;
    }

    @Override
    protected AssignmentStatus getAssignmentSearchStatus() {
        return AssignmentStatus.IN_PROGRESS;
    }

}
