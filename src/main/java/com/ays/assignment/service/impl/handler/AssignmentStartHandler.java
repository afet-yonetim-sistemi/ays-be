package com.ays.assignment.service.impl.handler;


import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.enums.AssignmentHandlerType;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.auth.model.AysIdentity;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.repository.UserRepository;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link AssignmentAbstractHandler} for {@link AssignmentHandlerType#START} type.
 * This class is responsible for handling the {@link AssignmentEntity} with {@link AssignmentStatus#ASSIGNED} status.
 */
@Component
class AssignmentStartHandler extends AssignmentAbstractHandler {

    private final UserRepository userRepository;

    public AssignmentStartHandler(AssignmentRepository assignmentRepository, UserRepository userRepository, AysIdentity identity) {
        super(assignmentRepository, identity);
        this.userRepository = userRepository;
    }

    @Override
    public AssignmentHandlerType type() {
        return AssignmentHandlerType.START;
    }


    @Override
    protected AssignmentEntity handle(AssignmentEntity assignment) {
        UserEntity user = assignment.getUser();
        user.onRoad();
        userRepository.save(user);

        assignment.start();
        return assignment;
    }

    @Override
    protected AssignmentStatus getAssignmentSearchStatus() {
        return AssignmentStatus.ASSIGNED;
    }

}
