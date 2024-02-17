package org.ays.assignment.service.impl.handler;

import org.ays.assignment.model.enums.AssignmentHandlerType;

/**
 * Assignment handler interface to handle assignment.
 */
public interface AssignmentHandler {

    AssignmentHandlerType type();

    void handle();

}
