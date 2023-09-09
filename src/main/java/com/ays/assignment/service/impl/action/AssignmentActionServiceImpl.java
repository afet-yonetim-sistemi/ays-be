package com.ays.assignment.service.impl.action;

import com.ays.assignment.model.enums.AssignmentActionEnum;
import com.ays.assignment.service.AssignmentActionService;
import com.ays.assignment.util.exception.AysAssignmentNotExistByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AssignmentActionServiceImpl implements AssignmentActionService {

    private final AssignmentActionContext assignmentActionContext;

    /**
     * Approve an assignment by their ID.
     *
     * @throws AysAssignmentNotExistByIdException if the assignment with the specified ID does not exist
     */
    @Override
    public void approveAssignment() {
        assignmentActionContext.executeAction(AssignmentActionEnum.APPROVE);
    }
}
