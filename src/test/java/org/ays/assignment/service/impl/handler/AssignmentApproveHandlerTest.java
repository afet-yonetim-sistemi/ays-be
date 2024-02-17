package org.ays.assignment.service.impl.handler;


import org.ays.AbstractUnitTest;
import org.ays.assignment.model.entity.AssignmentEntity;
import org.ays.assignment.model.entity.AssignmentEntityBuilder;
import org.ays.assignment.model.enums.AssignmentStatus;
import org.ays.assignment.repository.AssignmentRepository;
import org.ays.assignment.util.exception.AysAssignmentNotExistByUserIdAndStatusException;
import org.ays.auth.model.AysIdentity;
import org.ays.user.model.entity.UserEntity;
import org.ays.user.model.entity.UserEntityBuilder;
import org.ays.user.model.enums.UserSupportStatus;
import org.ays.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

class AssignmentApproveHandlerTest extends AbstractUnitTest {

    @InjectMocks
    private AssignmentApproveHandler assignmentApproveHandler;

    @Mock
    private AysIdentity identity;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void givenNothing_whenAssignmentApprove_thenReturnNothing() {

        // Given
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .build();
        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withUserId(mockUserEntity.getId())
                .withUser(mockUserEntity)
                .build();

        // When
        String userId = mockUserEntity.getId();
        String institutionId = mockUserEntity.getInstitutionId();
        AssignmentStatus assignmentStatus = AssignmentStatus.RESERVED;

        Mockito.when(identity.getUserId()).thenReturn(userId);
        Mockito.when(identity.getInstitutionId()).thenReturn(institutionId);
        Mockito.when(assignmentRepository.findByUserIdAndStatus(userId, assignmentStatus))
                .thenReturn(Optional.of(mockAssignmentEntity));
        Mockito.when(assignmentRepository.save(mockAssignmentEntity))
                .thenReturn(mockAssignmentEntity);
        Mockito.when(userRepository.save(mockUserEntity))
                .thenReturn(mockUserEntity);

        // Then
        assignmentApproveHandler.handle();

        Mockito.verify(identity, Mockito.times(1)).getUserId();
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByUserIdAndStatus(userId, assignmentStatus);
        Mockito.verify(assignmentRepository, Mockito.times(1)).save(mockAssignmentEntity);
    }

    @Test
    void givenNothing_whenAssignmentNotExistsForApproving_thenThrowsAysAssignmentUserNotStatusException() {

        // Given
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withSupportStatus(UserSupportStatus.READY)
                .build();
        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withUserId(mockUserEntity.getId())
                .withUser(mockUserEntity)
                .build();

        // When
        String userId = mockUserEntity.getId();
        String institutionId = mockUserEntity.getInstitutionId();
        AssignmentStatus assignmentStatus = AssignmentStatus.RESERVED;

        Mockito.when(identity.getUserId()).thenReturn(userId);
        Mockito.when(identity.getInstitutionId()).thenReturn(institutionId);
        Mockito.when(assignmentRepository.findByUserIdAndStatus(userId, assignmentStatus))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(AysAssignmentNotExistByUserIdAndStatusException.class, assignmentApproveHandler::handle);

        Mockito.verify(identity, Mockito.times(1)).getUserId();
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByUserIdAndStatus(userId, assignmentStatus);
        Mockito.verify(assignmentRepository, Mockito.times(0)).save(mockAssignmentEntity);
    }

}
