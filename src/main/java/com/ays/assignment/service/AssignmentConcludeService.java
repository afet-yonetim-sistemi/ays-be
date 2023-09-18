package com.ays.assignment.service;


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

}
