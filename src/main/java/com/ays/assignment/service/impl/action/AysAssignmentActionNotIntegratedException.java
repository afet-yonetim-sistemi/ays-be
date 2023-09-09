package com.ays.assignment.service.impl.action;

import com.ays.assignment.model.enums.AssignmentActionEnum;
import com.ays.common.util.exception.AysAuthException;

import java.io.Serial;

/**
 * Exception to be thrown when an assignment action is used with invalid name.
 */
public class AysAssignmentActionNotIntegratedException extends AysAuthException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = -4175558857868319572L;


    /**
     * Constructs a new AysAssignmentActionNotIntegratedException with the specified userId.
     *
     * @param assignmentAction The assignment action that not integrated to AssignmentActionContext.
     */
    public AysAssignmentActionNotIntegratedException(AssignmentActionEnum assignmentAction) {
        super("Assignment action not found. Action: " + assignmentAction);
    }

}
