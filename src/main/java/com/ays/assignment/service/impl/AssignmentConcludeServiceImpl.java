package com.ays.assignment.service.impl;

import com.ays.assignment.model.enums.AssignmentHandlerType;
import com.ays.assignment.service.AssignmentConcludeService;
import com.ays.assignment.service.impl.handler.AssignmentHandler;
import com.ays.assignment.util.exception.AysAssignmentActionNotIntegratedException;
import com.ays.assignment.util.exception.AysAssignmentNotExistByIdException;
import com.ays.common.util.exception.AysUnexpectedArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
class AssignmentConcludeServiceImpl implements AssignmentConcludeService {

    private final Set<AssignmentHandler> assignmentHandlers;

    /**
     * Approve an assignment by their ID.
     *
     * @throws AysAssignmentNotExistByIdException if the assignment with the specified ID does not exist
     */
    @Override
    public void approve() {
        this.findAssignmentHandler(AssignmentHandlerType.APPROVE).handle();
    }

    /**
     * Approve an assignment by their ID.
     *
     * @throws AysAssignmentNotExistByIdException if the assignment with the specified ID does not exist
     */
    @Override
    public void reject() {
        this.findAssignmentHandler(AssignmentHandlerType.REJECT).handle();
    }


    /**
     * Retrieve assignment action class by given assignment action name.
     *
     * @param actionType The type of assignment action that provided by AssignmentActionType
     * @return the assignment action implementation.
     * @throws AysAssignmentActionNotIntegratedException if given assignment action not implemented in assignmentActions.
     */
    private AssignmentHandler findAssignmentHandler(AssignmentHandlerType handlerType) {
        return assignmentHandlers.stream()
                .filter(assignmentHandler -> assignmentHandler.type().equals(handlerType))
                .findFirst()
                .orElseThrow(() -> new AysUnexpectedArgumentException(handlerType));
    }

}
