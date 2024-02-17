package org.ays.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ays.assignment.model.entity.AssignmentEntity;
import org.ays.assignment.model.enums.AssignmentStatus;
import org.ays.assignment.repository.AssignmentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Scheduled task to regulate reserved assignments by updating them to available status.
 * <p>
 * This scheduler is enabled based on the configuration property
 * {@code ays.scheduler.reserved-assignments-regulation.enable}.
 *
 * @see AssignmentRepository
 * @see AssignmentEntity
 * @see AssignmentStatus
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "ays.scheduler.reserved-assignments-regulation.enable", havingValue = "true")
class ReservedAssignmentsRegulationScheduler {

    private final AssignmentRepository assignmentRepository;

    /**
     * Scheduled task method to update reserved assignments to available status.
     * This method is triggered based on the cron expression specified in the configuration property
     * {@code ays.scheduler.reserved-assignments-regulation.cron}.
     * <p>
     * The method fetches all reserved assignments created before the current time minus 20 seconds
     * and updates their status to {@link AssignmentStatus#AVAILABLE}.
     *
     * @see AssignmentRepository#findAllByStatusAndCreatedAtBefore(AssignmentStatus, LocalDateTime)
     * @see AssignmentEntity
     * @see AssignmentStatus
     */
    @Transactional
    @Scheduled(cron = "${ays.scheduler.reserved-assignments-regulation.cron}")
    public void updateReservedAssignmentsToAvailable() {
        log.trace("Updating all reserved assignments to available...");

        List<AssignmentEntity> assignmentEntities = assignmentRepository.findAllByStatusAndCreatedAtBefore(
                AssignmentStatus.RESERVED,
                LocalDateTime.now().minusSeconds(20)
        );

        assignmentEntities.forEach(assignment -> assignment.setStatus(AssignmentStatus.AVAILABLE));

        assignmentRepository.saveAll(assignmentEntities);

        log.trace("All reserved assignments are updated to available.");
    }

}
