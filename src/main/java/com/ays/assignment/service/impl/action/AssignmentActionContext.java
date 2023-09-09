package com.ays.assignment.service.impl.action;

import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.enums.AssignmentActionEnum;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.service.impl.action.strategy.AssignmentAction;
import com.ays.assignment.service.impl.action.strategy.impl.AssignmentApproveAction;
import com.ays.assignment.util.exception.AysAssignmentUserNotReservedException;
import com.ays.auth.model.AysIdentity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class AssignmentActionContext {
    private final AssignmentRepository assignmentRepository;
    private final AysIdentity aysIdentity;

    private final static Map<AssignmentActionEnum, AssignmentAction> assignmentActions = Map.of(
            AssignmentActionEnum.APPROVE, new AssignmentApproveAction()
    );

    /**
     * Retrieve assignment action class by given assignment action name.
     *
     * @param assignmentAction The name of assignment action that provided by AssignmentActionEnum
     * @return the assignment action implementation.
     * @throws AysAssignmentActionNotIntegratedException if given assignment action not implemented in assignmentActions.
     */
    public AssignmentAction getAction(AssignmentActionEnum assignmentAction) {
        return Optional
                .ofNullable(assignmentActions.get(assignmentAction))
                .orElseThrow(() -> new AysAssignmentActionNotIntegratedException(assignmentAction));
    }

    /**
     * Run assignment action by given assignment action name.
     *
     * @param assignmentAction The name of assignment action that provided by AssignmentActionEnum
     * @throws AysAssignmentUserNotReservedException if current user does not have reserved assignment
     */
    public void executeAction(AssignmentActionEnum assignmentAction) {
        String userId = aysIdentity.getUserId();
        AssignmentEntity assignmentEntity = assignmentRepository
                .findByUserIdAndStatus(userId, AssignmentStatus.RESERVED)
                .orElseThrow(() -> new AysAssignmentUserNotReservedException(userId));

        assignmentEntity = getAction(assignmentAction).execute(assignmentEntity);
        assignmentRepository.save(assignmentEntity);
    }


}
