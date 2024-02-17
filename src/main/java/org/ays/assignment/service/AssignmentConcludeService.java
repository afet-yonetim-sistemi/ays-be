package org.ays.assignment.service;


import org.ays.assignment.model.dto.request.AssignmentCancelRequest;

/**
 * Assignment Action Service to perform assignment related business operations.
 */
public interface AssignmentConcludeService {

    /**
     * Approves an assignment that is reserved by user
     */
    void approve();

    /**
     * Reject an assignment that is reserved by user
     */
    void reject();

    /**
     * Start an assignment that is approved by user
     */
    void start();

    /**
     * Complete an assignment that is started by user
     */
    void complete();

    /**
     * Cancel an assignment that is either started by user or assigned to user
     */
    void cancel(AssignmentCancelRequest cancelRequest);
}
