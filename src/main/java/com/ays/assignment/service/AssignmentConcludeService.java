package com.ays.assignment.service;


/**
 * Assignment Action Service to perform assignment related business operations.
 */
public interface AssignmentConcludeService {

    /**
     * Approve Assignment by Assignment ID
     */
    void approve();

    /**
     * Reject Assignment by Assignment ID
     */
    void reject();

}
