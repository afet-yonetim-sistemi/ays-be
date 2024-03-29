package org.ays.assignment.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.assignment.model.dto.request.AssignmentCancelRequest;
import org.ays.assignment.model.enums.AssignmentHandlerType;
import org.ays.assignment.service.AssignmentConcludeService;
import org.ays.assignment.service.impl.handler.AssignmentHandler;
import org.ays.assignment.util.exception.AysAssignmentNotExistByUserIdAndStatusException;
import org.ays.common.util.exception.AysUnexpectedArgumentException;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Implementation of {@link AssignmentConcludeService}.
 * This class is responsible for handling assignment actions such as approve, reject, start and complete.
 */
@Service
@RequiredArgsConstructor
class AssignmentConcludeServiceImpl implements AssignmentConcludeService {

    private final Set<AssignmentHandler> assignmentHandlers;

    /**
     * Approve an assignment by their ID.
     *
     * @throws AysAssignmentNotExistByUserIdAndStatusException if the assignment with the specified userId and assignmentStatus does not exist
     */
    @Override
    public void approve() {
        this.findAssignmentHandler(AssignmentHandlerType.APPROVE).handle();
    }

    /**
     * Approve an assignment by their ID.
     *
     * @throws AysAssignmentNotExistByUserIdAndStatusException if the assignment with the specified userId and assignmentStatus does not exist
     */
    @Override
    public void reject() {
        this.findAssignmentHandler(AssignmentHandlerType.REJECT).handle();
    }

    /**
     * Start an assignment by their ID.
     *
     * @throws AysAssignmentNotExistByUserIdAndStatusException if the assignment with the specified userId and assignmentStatus does not exist
     */
    @Override
    public void start() {
        this.findAssignmentHandler(AssignmentHandlerType.START).handle();
    }

    /**
     * Complete an assignment by their ID.
     *
     * @throws AysAssignmentNotExistByUserIdAndStatusException if the assignment with the specified userId and assignmentStatus does not exist
     */
    @Override
    public void complete() {
        this.findAssignmentHandler(AssignmentHandlerType.COMPLETE).handle();
    }

    /**
     * Cancel an assignment by the authenticated user ID.
     *
     * @throws AysAssignmentNotExistByUserIdAndStatusException if the assignment with the specified userId and assignmentStatus does not exist
     */
    @Override
    public void cancel(AssignmentCancelRequest cancelRequest) {
        this.findAssignmentHandler(AssignmentHandlerType.CANCEL).handle();
    }

    /**
     * Retrieve assignment action class by given assignment action name.
     *
     * @param handlerType The type of assignment action that provided by AssignmentHandlerType
     * @return the assignment action implementation.
     * @throws AysUnexpectedArgumentException if given assignment action not implemented in assignmentActions.
     */
    private AssignmentHandler findAssignmentHandler(AssignmentHandlerType handlerType) {
        return assignmentHandlers.stream()
                .filter(assignmentHandler -> assignmentHandler.type().equals(handlerType))
                .findFirst()
                .orElseThrow(() -> new AysUnexpectedArgumentException(handlerType));
    }

}
