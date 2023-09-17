package com.ays.assignment.service.impl.handler;

import com.ays.AbstractUnitTest;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.entity.AssignmentEntityBuilder;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.util.exception.AysAssignmentUserNotStatusException;
import com.ays.auth.model.AysIdentity;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.user.model.enums.UserSupportStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;


class AssignmentStartHandlerTest extends AbstractUnitTest {

    @InjectMocks
    private AssignmentStartHandler assignmentStartHandler;

    @Mock
    private AysIdentity identity;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Test
    void givenNothing_whenAssignmentStart_thenReturnNothing() {

        // Given
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withUserSupportStatus(UserSupportStatus.READY)
                .build();
        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withUserId(mockUserEntity.getId())
                .withUser(mockUserEntity)
                .build();

        // When
        String userId = mockUserEntity.getId();
        String institutionId = mockUserEntity.getInstitutionId();
        List<AssignmentStatus> assignmentStatuses = List.of(AssignmentStatus.ASSIGNED);

        Mockito.when(identity.getUserId()).thenReturn(userId);
        Mockito.when(identity.getInstitutionId()).thenReturn(institutionId);
        Mockito.when(assignmentRepository.findByUserIdAndStatusIsIn(userId, assignmentStatuses))
                .thenReturn(Optional.of(mockAssignmentEntity));
        Mockito.when(assignmentRepository.save(mockAssignmentEntity))
                .thenReturn(mockAssignmentEntity);

        // Then
        assignmentStartHandler.handle();

        Mockito.verify(identity, Mockito.times(1)).getUserId();
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByUserIdAndStatusIsIn(userId, assignmentStatuses);
        Mockito.verify(assignmentRepository, Mockito.times(1)).save(mockAssignmentEntity);
    }

    @Test
    void givenNothing_whenAssignmentNotExistsForStarting_thenThrowsAysAssignmentUserNotStatusException() {

        // Given
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withUserSupportStatus(UserSupportStatus.READY)
                .build();
        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withUserId(mockUserEntity.getId())
                .withUser(mockUserEntity)
                .build();

        // When
        String userId = mockUserEntity.getId();
        String institutionId = mockUserEntity.getInstitutionId();
        List<AssignmentStatus> assignmentStatuses = List.of(AssignmentStatus.ASSIGNED);

        Mockito.when(identity.getUserId()).thenReturn(userId);
        Mockito.when(identity.getInstitutionId()).thenReturn(institutionId);
        Mockito.when(assignmentRepository.findByUserIdAndStatusIsIn(userId, assignmentStatuses))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(AysAssignmentUserNotStatusException.class, assignmentStartHandler::handle);

        Mockito.verify(identity, Mockito.times(1)).getUserId();
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByUserIdAndStatusIsIn(userId, assignmentStatuses);
        Mockito.verify(assignmentRepository, Mockito.times(0)).save(mockAssignmentEntity);
    }

}