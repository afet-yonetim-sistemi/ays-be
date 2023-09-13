package com.ays.assignment.service;


import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.dto.request.AssignmentListRequest;
import com.ays.assignment.model.dto.request.AssignmentUpdateRequest;
import com.ays.common.model.AysPage;

/**
 * Assignment Save Service to perform assignment related business operations.
 */
public interface AssignmentService {

    /**
     * Get Assignments based on the specified filters in the {@link AssignmentListRequest}
     *
     * @param listRequest The request dto object
     * @return Assignments list
     */
    AysPage<Assignment> getAssignments(AssignmentListRequest listRequest);

    /**
     * Get Assignment by Assignment ID
     *
     * @param id the given Assignment ID
     * @return Assignment
     */
    Assignment getAssignmentById(String id);

    /**
     * Update Assignment by Assignment ID and {@link AssignmentUpdateRequest}
     *
     * @param id            the given Assignment ID
     * @param updateRequest the request that contains new assignment information
     */
    void updateAssignment(String id, AssignmentUpdateRequest updateRequest);

}
