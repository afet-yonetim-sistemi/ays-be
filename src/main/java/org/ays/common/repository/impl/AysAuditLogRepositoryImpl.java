package org.ays.common.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.ays.common.model.entity.AysAuditLogEntity;
import org.ays.common.repository.AysAuditLogRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 * Default implementation of the {@link AysAuditLogRepository} interface.
 * This implementation logs audit log details to the application logs for debugging or local development.
 * It is active only when the application is running with the "default" profile.
 */
@Slf4j
@Repository
@Profile("default")
public class AysAuditLogRepositoryImpl implements AysAuditLogRepository {

    /**
     * Saves the given {@link AysAuditLogEntity} and logs its JSON representation.
     *
     * @param auditLogEntity the audit log entity to be saved
     */
    @Override
    public void save(AysAuditLogEntity auditLogEntity) {
        log.debug("Audit log saved: {}", auditLogEntity.toKinesisJsonString());
    }

}
