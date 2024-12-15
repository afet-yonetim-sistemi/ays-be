package org.ays.common.repository;

import org.ays.common.model.entity.AysAuditLogEntity;

/**
 * Repository interface for handling persistence operations related to {@link AysAuditLogEntity}.
 * Provides an abstraction layer for saving audit log entities to a data source.
 */
public interface AysAuditLogRepository {

    /**
     * Persists an {@link AysAuditLogEntity} to the underlying data store.
     *
     * @param entity the audit log entity to be saved
     */
    void save(AysAuditLogEntity entity);

}
