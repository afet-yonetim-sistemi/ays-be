package org.ays.auth.model.enums;

/**
 * Enum representing the status of an administrative registration application.
 * Possible statuses include:
 * <ul>
 *     <li>{@link #WAITING}: The application is waiting for review.</li>
 *     <li>{@link #COMPLETED}: The application has been completed and is pending approval.</li>
 *     <li>{@link #APPROVED}: The application has been approved.</li>
 *     <li>{@link #REJECTED}: The application has been rejected.</li>
 * </ul>
 */
public enum AdminRegistrationApplicationStatus {

    WAITING,
    COMPLETED,
    APPROVED,
    REJECTED

}
