package org.ays.assignment.service;


import org.ays.assignment.model.Assignment;
import org.ays.assignment.model.dto.request.AssignmentListRequest;
import org.ays.assignment.model.dto.request.AssignmentUpdateRequest;
import org.ays.common.model.AysPage;

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
     * Get Assignment that is assigned to the current user
     *
     * @return Assignment
     */
    Assignment getUserAssignment();

    /**
     * Get Assignment by Assignment ID
     *
     * @param id the given Assignment ID
     * @return Assignment
     */
    Assignment getAssignmentById(String id);

    /**
     * Get Assignment Summary based on the logged-in user
     *
     * @return Assignment
     */
    Assignment getAssignmentSummary();

    /**
     * Update Assignment by Assignment ID and {@link AssignmentUpdateRequest}
     *
     * @param id            the given Assignment ID
     * @param updateRequest the request that contains new assignment information
     */
    void updateAssignment(String id, AssignmentUpdateRequest updateRequest);

    /**
     * Delete assignment by Assignment ID
     *
     * @param id the given AssignmentID
     */
    void deleteAssignment(final String id);
}
