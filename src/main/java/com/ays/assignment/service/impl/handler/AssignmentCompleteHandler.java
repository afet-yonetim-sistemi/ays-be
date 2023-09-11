package com.ays.assignment.service.impl.handler;


import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.enums.AssignmentHandlerType;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.auth.model.AysIdentity;
import org.springframework.stereotype.Component;

@Component
class AssignmentCompleteHandler extends AssignmentAbstractHandler {

    public AssignmentCompleteHandler(AssignmentRepository assignmentRepository, AysIdentity aysIdentity) {
        super(assignmentRepository, aysIdentity);
    }

    @Override
    public AssignmentHandlerType type() {
        return AssignmentHandlerType.COMPLETE;
    }

    @Override
    protected AssignmentEntity handle(AssignmentEntity assignmentEntity) {
        return AssignmentEntity.builder().build(); // TODO : implement
    }

}
