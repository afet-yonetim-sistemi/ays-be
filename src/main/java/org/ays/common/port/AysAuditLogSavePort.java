package org.ays.common.port;

import org.ays.common.model.AysAuditLog;

/**
 * This interface defines a contract for saving audit logs in the system.
 * Implementations of this interface are responsible for persisting audit log
 * entries to a specified storage medium, such as a database, file system, or
 * external service.
 */
public interface AysAuditLogSavePort {

    /**
     * Saves the provided audit log.
     *
     * @param auditLog the {@link AysAuditLog} instance to be saved.
     *                 It should contain all necessary information regarding
     *                 the action being logged.
     */
    void save(AysAuditLog auditLog);

}
