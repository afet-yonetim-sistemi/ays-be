package org.ays.emergency_application.model.entity;

/**
 * Enumeration keeping emergency evacuation status.
 */
public enum EmergencyEvacuationApplicationStatus {
    PENDING,

    IN_REVIEW,

    IN_PROGRESS,

    RECEIVED_FIRST_APPROVE,

    RECEIVED_SECOND_APPROVE,

    RECEIVED_THIRD_APPROVE,

    COMPLETED,

    CANCELLED
}
