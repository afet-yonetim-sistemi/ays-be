package com.ays.assignment.service.impl.handler;


import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.enums.AssignmentHandlerType;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.auth.model.AysIdentity;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
class AssignmentApproveHandler extends AssignmentAbstractHandler {

    private final UserRepository userRepository;

    public AssignmentApproveHandler(AssignmentRepository assignmentRepository, UserRepository userRepository, AysIdentity identity) {
        super(assignmentRepository, identity);
        this.userRepository = userRepository;
    }

    @Override
    public AssignmentHandlerType type() {
        return AssignmentHandlerType.APPROVE;
    }


    @Override
    protected AssignmentEntity handle(AssignmentEntity assignment) {
        UserEntity user = assignment.getUser();
        user.busy();
        userRepository.save(user);

        assignment.assign();
        return assignment;
    }

    @Override
    protected AssignmentStatus getAssignmentSearchStatus() {
        return AssignmentStatus.RESERVED;
    }

}
