package org.ays.common.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.ays.common.model.entity.AysAuditLogEntity;
import org.ays.common.repository.AysAuditLogRepository;

/**
 * Abstract base implementation of the {@link AysAuditLogRepository} interface.
 * <p>
 * This class provides a default implementation for saving audit log entities by logging their JSON representation.
 * It does not persist the audit logs to a data store but can be extended by subclasses that require additional
 * persistence logic.
 * </p>
 *
 * @see AysAuditLogRepository
 */
@Slf4j
abstract class AysAbstractAuditLogRepository implements AysAuditLogRepository {

    /**
     * Saves the given audit log entity.
     * <p>
     * The default implementation logs the JSON representation of the audit log entity at the debug level.
     * Subclasses may override this method to provide actual persistence mechanisms if needed.
     * </p>
     *
     * @param auditLogEntity the audit log entity to be saved
     */
    @Override
    public void save(final AysAuditLogEntity auditLogEntity) {
        log.debug("Audit log saved: {}", auditLogEntity.toJsonString());
    }

}
