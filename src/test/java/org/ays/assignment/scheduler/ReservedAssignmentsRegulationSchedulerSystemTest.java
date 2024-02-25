package org.ays.assignment.scheduler;

import org.awaitility.Awaitility;
import org.ays.AbstractSystemTest;
import org.ays.assignment.model.entity.AssignmentEntity;
import org.ays.assignment.model.entity.AssignmentEntityBuilder;
import org.ays.assignment.model.enums.AssignmentStatus;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;
import org.ays.user.model.entity.UserEntity;
import org.ays.user.model.entity.UserEntityBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

class ReservedAssignmentsRegulationSchedulerSystemTest extends AbstractSystemTest {


    @Value("${ays.scheduler.reserved-assignments-regulation.cron}")
    private String expectedCronExpression;

    @Autowired
    private ScheduledTaskHolder taskHolder;

    @SpyBean
    private ReservedAssignmentsRegulationScheduler reservedAssignmentsRegulationScheduler;

    private void initialize(InstitutionEntity institutionEntity,
                            UserEntity userEntity,
                            List<AssignmentEntity> mockAssignmentEntities) {

        institutionRepository.save(institutionEntity);
        userRepository.save(userEntity);
        assignmentRepository.saveAll(mockAssignmentEntities);
    }

    @Test
    void whenCronReservedAssignmentsRegulationSchedulerTaskCreated_thenCronTaskScheduled() {
        // Then
        CronTask cronTask = taskHolder.getScheduledTasks()
                .stream()
                .map(ScheduledTask::getTask)
                .filter(CronTask.class::isInstance)
                .map(CronTask.class::cast)
                .filter(task -> task.toString().contains("common.scheduler.ReservedAssignmentsRegulationScheduler"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No scheduled tasks"));

        Assertions.assertEquals(expectedCronExpression, cronTask.getExpression());
    }

    @Test
    void whenWait10Seconds_thenClearInvalidToken() {

        // Initialize
        InstitutionEntity mockInstitutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withInstitutionId(mockInstitutionEntity.getId())
                .withInstitution(null)
                .build();
        List<AssignmentEntity> mockAssignmentEntities = List.of(
                new AssignmentEntityBuilder()
                        .withValidFields()
                        .withCreatedAt(LocalDateTime.now().minusSeconds(20))
                        .withInstitutionId(mockInstitutionEntity.getId())
                        .withInstitution(null)
                        .withUserId(mockUserEntity.getId())
                        .withUser(null)
                        .build(),
                new AssignmentEntityBuilder()
                        .withValidFields()
                        .withCreatedAt(LocalDateTime.now().minusSeconds(20))
                        .withInstitutionId(mockInstitutionEntity.getId())
                        .withInstitution(null)
                        .withUserId(mockUserEntity.getId())
                        .withUser(null)
                        .build()
        );
        this.initialize(mockInstitutionEntity, mockUserEntity, mockAssignmentEntities);

        // Then
        Awaitility
                .await()
                .atMost(2, java.util.concurrent.TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Mockito.verify(reservedAssignmentsRegulationScheduler, Mockito.atLeast(1))
                            .updateReservedAssignmentsToAvailable();

                    mockAssignmentEntities.forEach(mockAssignmentEntity -> {
                        Optional<AssignmentEntity> assignmentEntity = assignmentRepository
                                .findById(mockAssignmentEntity.getId());

                        Assertions.assertTrue(assignmentEntity.isPresent());
                        Assertions.assertEquals(AssignmentStatus.AVAILABLE, assignmentEntity.get().getStatus());
                    });
                });

    }

}
