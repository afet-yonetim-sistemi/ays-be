package com.ays.assignment.service.impl.handler;

import com.ays.assignment.model.enums.AssignmentHandlerType;

/**
 * Assignment handler interface to handle assignment.
 */
public interface AssignmentHandler {

    AssignmentHandlerType type();

    void handle();

}
