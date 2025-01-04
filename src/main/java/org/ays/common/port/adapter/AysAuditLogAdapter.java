package org.ays.common.port.adapter;

import lombok.RequiredArgsConstructor;
import org.ays.common.model.AysAuditLog;
import org.ays.common.model.entity.AysAuditLogEntity;
import org.ays.common.model.mapper.AysAuditLogToEntityMapper;
import org.ays.common.port.AysAuditLogSavePort;
import org.ays.common.repository.AysAuditLogRepository;
import org.springframework.stereotype.Component;

/**
 * Adapter for saving {@link AysAuditLog} entries into the audit log database.
 * This class implements the {@link AysAuditLogSavePort} interface
 * and utilizes the {@link AysAuditLogRepository} to persist audit log entities.
 * It also uses a mapper to convert domain objects to entity objects.
 */
@Component
@RequiredArgsConstructor
public class AysAuditLogAdapter implements AysAuditLogSavePort {

    private final AysAuditLogRepository auditLogRepository;


    private final AysAuditLogToEntityMapper auditLogToEntityMapper = AysAuditLogToEntityMapper.initialize();


    /**
     * Saves the given {@link AysAuditLog} into the repository.
     *
     * @param auditLog the audit log entry to be saved
     */
    @Override
    public void save(final AysAuditLog auditLog) {
        final AysAuditLogEntity auditLogEntity = auditLogToEntityMapper.map(auditLog);
        auditLogRepository.save(auditLogEntity);
    }

}
