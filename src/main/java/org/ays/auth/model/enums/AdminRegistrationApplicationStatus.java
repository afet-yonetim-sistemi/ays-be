package org.ays.auth.model.enums;

/**
 * Represents the status of an admin registration application.
 * <p>
 * Possible statuses include:
 * <ul>
 *   <li>WAITING - The application is pending review.</li>
 *   <li>COMPLETED - The application has been completed.</li>
 *   <li>VERIFIED - The application has been verified.</li>
 *   <li>REJECTED - The application has been rejected.</li>
 * </ul>
 */
public enum AdminRegistrationApplicationStatus {

    WAITING,
    COMPLETED,
    VERIFIED,
    REJECTED

}
