package com.ays.assignment.service;

import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.dto.request.AssignmentListRequest;
import com.ays.assignment.model.dto.request.AssignmentUpdateRequest;
import com.ays.common.model.AysPage;

/**
 * Assignment service to perform assignment related business operations.
 */
public interface AssignmentService {

    /**
     * Get all Assignment from database.
     *
     * @param listRequest covering page and pageSize
     * @return Assignment list
     */
    AysPage<Assignment> getAllAssignments(AssignmentListRequest listRequest);

    /**
     * Get Assignment by Assignment ID
     *
     * @param id the given Assignment ID
     * @return Assignment
     */
    Assignment getAssignmentById(String id);

    /**
     * Update Assignment by Assignment ID
     *
     * @param id            the given Assignment ID
     * @param updateRequest the given AssignmentUpdateRequest object
     */
    void updateAssignment(String id, AssignmentUpdateRequest updateRequest);


    /**
     * Delete Assignment by Assignment ID
     *
     * @param id the given Assignment ID
     */
    void deleteAssignment(String id);

}
