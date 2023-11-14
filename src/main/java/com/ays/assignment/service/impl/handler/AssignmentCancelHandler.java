package com.ays.assignment.service.impl.handler;

import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.enums.AssignmentHandlerType;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.util.exception.AysAssignmentNotExistByUserIdAndStatusException;
import com.ays.auth.model.AysIdentity;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
@RequiredArgsConstructor
public class AssignmentCancelHandler implements AssignmentHandler {

    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final AysIdentity identity;

    @Override
    public AssignmentHandlerType type() {
        return AssignmentHandlerType.CANCEL;
    }

    @Override
    public void handle() {
        final AssignmentEntity assignmentEntity = this.findAssignmentEntity();
        final UserEntity userEntity = assignmentEntity.getUser();
        userEntity.busy();
        userRepository.save(userEntity);

        assignmentEntity.available();
        assignmentRepository.save(assignmentEntity);
    }

    private AssignmentEntity findAssignmentEntity() {
        final String userId = identity.getUserId();
        final EnumSet<AssignmentStatus> acceptedStatuses = EnumSet.of(AssignmentStatus.ASSIGNED, AssignmentStatus.IN_PROGRESS);
        return assignmentRepository.findByUserIdAndStatusIn(userId, acceptedStatuses)
                .orElseThrow(() -> new AysAssignmentNotExistByUserIdAndStatusException(userId, acceptedStatuses));
    }
}
