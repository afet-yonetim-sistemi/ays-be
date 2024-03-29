package org.ays.assignment.scheduler;

import org.ays.AbstractUnitTest;
import org.ays.assignment.model.entity.AssignmentEntity;
import org.ays.assignment.model.entity.AssignmentEntityBuilder;
import org.ays.assignment.model.enums.AssignmentStatus;
import org.ays.assignment.repository.AssignmentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

class ReservedAssignmentsRegulationSchedulerTest extends AbstractUnitTest {

    @InjectMocks
    private ReservedAssignmentsRegulationScheduler reservedAssignmentsRegulationScheduler;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Test
    void whenReservedAssignmentsUpdateToAvailableByBefore20Seconds_thenSaveAll() {

        // When
        List<AssignmentEntity> mockAssignmentEntities = List.of(
                new AssignmentEntityBuilder()
                        .withValidFields()
                        .withCreatedAt(LocalDateTime.now().minusSeconds(20))
                        .build(),
                new AssignmentEntityBuilder()
                        .withValidFields()
                        .withCreatedAt(LocalDateTime.now().minusSeconds(20))
                        .build()
        );
        Mockito.when(assignmentRepository.findAllByStatusAndCreatedAtBefore(
                Mockito.any(AssignmentStatus.class),
                Mockito.any(LocalDateTime.class)
        )).thenReturn(mockAssignmentEntities);

        // Then
        reservedAssignmentsRegulationScheduler.updateReservedAssignmentsToAvailable();

        // Verify
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findAllByStatusAndCreatedAtBefore(
                        Mockito.any(AssignmentStatus.class),
                        Mockito.any(LocalDateTime.class)
                );
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .saveAll(Mockito.anyList());
    }

}
