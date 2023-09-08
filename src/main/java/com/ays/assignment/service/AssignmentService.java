package com.ays.assignment.service;


import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.dto.request.AssignmentUpdateRequest;

/**
 * Assignment Save Service to perform assignment related business operations.
 */
public interface AssignmentService {

    /**
     * Get Assignment by Assignment ID
     *
     * @param id the given Assignment ID
     * @return Assignment
     */
    Assignment getAssignmentById(String id);

    void updateAssignment(final String id, AssignmentUpdateRequest updateRequest);

}
