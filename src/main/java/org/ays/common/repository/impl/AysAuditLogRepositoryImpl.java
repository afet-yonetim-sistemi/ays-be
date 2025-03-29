package org.ays.common.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.ays.common.repository.AysAuditLogRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 * Default implementation of the {@link AysAuditLogRepository} interface.
 * <p>
 * This class extends {@link AysAbstractAuditLogRepository} and serves as the default repository
 * implementation for audit log persistence when the "default" Spring profile is active.
 * It leverages the logging-based persistence provided by the abstract superclass.
 * </p>
 */
@Slf4j
@Repository
@Profile("default")
public class AysAuditLogRepositoryImpl extends AysAbstractAuditLogRepository {
}
