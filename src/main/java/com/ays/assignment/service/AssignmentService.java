package com.ays.assignment.service;


import com.ays.assignment.model.Assignment;

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
}
