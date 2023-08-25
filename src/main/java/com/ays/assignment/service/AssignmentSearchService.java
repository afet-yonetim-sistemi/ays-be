package com.ays.assignment.service;

import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.dto.request.AssignmentSearchRequest;


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
