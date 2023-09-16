package com.ays.assignment.service.impl.handler;


import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.enums.AssignmentHandlerType;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.auth.model.AysIdentity;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.enums.UserSupportStatus;
import com.ays.user.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssignmentRejectHandler extends AssignmentAbstractHandler {

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
        user.setSupportStatus(UserSupportStatus.READY);
        userRepository.save(user);
        assignment.updateAssignmentStatus(AssignmentStatus.AVAILABLE);
        assignment.setUserId(null);
        return assignment;
    }


    @Override
    protected AssignmentEntity findAssignmentEntity() {
        return this.findAssignmentByStatuses(List.of(
                AssignmentStatus.RESERVED,
                AssignmentStatus.ASSIGNED,
                AssignmentStatus.IN_PROGRESS
        ));
    }
}
