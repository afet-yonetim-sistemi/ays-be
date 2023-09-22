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
 * Implementation of {@link AssignmentAbstractHandler} for {@link AssignmentHandlerType#REJECT} type.
 * This class is responsible for handling the {@link AssignmentEntity} with {@link AssignmentStatus#RESERVED} status.
 */
@Component
class AssignmentRejectHandler extends AssignmentAbstractHandler {

    private final UserRepository userRepository;

    public AssignmentRejectHandler(AssignmentRepository assignmentRepository, UserRepository userRepository, AysIdentity identity) {
        super(assignmentRepository, identity);
        this.userRepository = userRepository;
    }

    @Override
    public AssignmentHandlerType type() {
        return AssignmentHandlerType.REJECT;
    }


    @Override
    protected AssignmentEntity handle(AssignmentEntity assignment) {
        UserEntity user = assignment.getUser();
        user.ready();
        userRepository.save(user);

        assignment.available();
        return assignment;
    }

    @Override
    protected AssignmentStatus getAssignmentSearchStatus() {
        return AssignmentStatus.RESERVED;
    }

}
