package com.ays.common.scheduler;

import com.ays.AbstractUnitTest;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.entity.AssignmentEntityBuilder;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.repository.AssignmentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

class ReservedAssignmentsRegulationSchedulerTest extends AbstractUnitTest {

    @InjectMocks
    private ReservedAssignmentsRegulationScheduler reservedAssignmentsRegulationSchedulerMock;

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
        reservedAssignmentsRegulationSchedulerMock.updateReservedAssignmentsToAvailable();

        // Verify
        Mockito.verify(assignmentRepository, Mockito.atLeast(1))
                .findAllByStatusAndCreatedAtBefore(
                        Mockito.any(AssignmentStatus.class),
                        Mockito.any(LocalDateTime.class)
                );
        Mockito.verify(assignmentRepository, Mockito.atLeast(1))
                .saveAll(Mockito.anyList());
    }

}
