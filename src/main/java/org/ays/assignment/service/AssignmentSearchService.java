package org.ays.assignment.service;

import org.ays.assignment.model.Assignment;
import org.ays.assignment.model.dto.request.AssignmentSearchRequest;


/**
 * Assignment Search Service to perform assignment related search business operations.
 */
public interface AssignmentSearchService {

    /**
     * Search nearest assignment by AssignmentSearchRequest
     *
     * @param assignmentSearchRequest the AssignmentSearchRequest
     * @return Assignment
     */
    Assignment searchAssignment(AssignmentSearchRequest assignmentSearchRequest);
}
