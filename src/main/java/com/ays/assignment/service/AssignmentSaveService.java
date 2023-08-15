package com.ays.assignment.service;

import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.dto.request.AssignmentSaveRequest;

/**
 * Assignment Save Service to perform assignment related save business operations.
 */
public interface AssignmentSaveService {

    /**
     * Saves a saveRequest to the database.
     *
     * @param saveRequest the AssignmentSaveRequest entity
     * @return Assignment
     */
    Assignment saveAssignment(AssignmentSaveRequest saveRequest);
}
