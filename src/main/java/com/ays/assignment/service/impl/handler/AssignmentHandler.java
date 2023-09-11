package com.ays.assignment.service.impl.handler;

import com.ays.assignment.model.enums.AssignmentHandlerType;

public interface AssignmentHandler {

    AssignmentHandlerType type();

    void handle();

}
