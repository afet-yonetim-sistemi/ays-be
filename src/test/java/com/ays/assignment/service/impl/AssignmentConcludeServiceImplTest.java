package com.ays.assignment.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.entity.AssignmentEntityBuilder;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.service.impl.handler.*;
import com.ays.assignment.util.exception.AysAssignmentUserNotStatusException;
import com.ays.auth.model.AysIdentity;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.user.model.enums.UserSupportStatus;
import com.ays.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class AssignmentConcludeServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AssignmentConcludeServiceImpl assignmentConcludeService;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AysIdentity identity;

    @InjectMocks
    private AssignmentApproveHandler assignmentApproveHandler;

    @InjectMocks
    private AssignmentStartHandler assignmentStartHandler;

    @InjectMocks
    private AssignmentRejectHandler assignmentRejectHandler;

    @InjectMocks
    private AssignmentCompleteHandler assignmentCompleteHandler;

    @Mock
    private Set<AssignmentHandler> assignmentHandlers;

    @BeforeEach
    public void setUp() {
        Mockito
                .when(assignmentHandlers.stream())
                .thenReturn(Stream.of(
                        assignmentApproveHandler,
                        assignmentStartHandler,
                        assignmentRejectHandler,
                        assignmentCompleteHandler
                ));
    }

    @Test
    void givenVoidRequest_whenAssignmentApprove_thenReturnVoid() {

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
        List<AssignmentStatus> assignmentStatuses = List.of(AssignmentStatus.RESERVED);

        Mockito.when(identity.getUserId()).thenReturn(userId);
        Mockito.when(identity.getInstitutionId()).thenReturn(institutionId);
        Mockito.when(assignmentRepository.findByUserIdAndStatusIsIn(userId, assignmentStatuses))
                .thenReturn(Optional.of(mockAssignmentEntity));
        Mockito.when(assignmentRepository.save(mockAssignmentEntity))
                .thenReturn(mockAssignmentEntity);

        // Then
        assignmentConcludeService.approve();

        Mockito.verify(identity, Mockito.times(1)).getUserId();
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByUserIdAndStatusIsIn(userId, assignmentStatuses);
        Mockito.verify(assignmentRepository, Mockito.times(1)).save(mockAssignmentEntity);
    }

    @Test
    void givenVoidRequest_whenAssignmentNotExistsForApproving_thenThrowsAysAssignmentUserNotStatusException() {

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
        List<AssignmentStatus> assignmentStatuses = List.of(AssignmentStatus.RESERVED);

        Mockito.when(identity.getUserId()).thenReturn(userId);
        Mockito.when(identity.getInstitutionId()).thenReturn(institutionId);
        Mockito.when(assignmentRepository.findByUserIdAndStatusIsIn(userId, assignmentStatuses))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(AysAssignmentUserNotStatusException.class, assignmentConcludeService::approve);

        Mockito.verify(identity, Mockito.times(1)).getUserId();
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByUserIdAndStatusIsIn(userId, assignmentStatuses);
        Mockito.verify(assignmentRepository, Mockito.times(0)).save(mockAssignmentEntity);
    }

    @Test
    void givenVoidRequest_whenAssignmentStart_thenReturnVoid() {

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
        assignmentConcludeService.start();

        Mockito.verify(identity, Mockito.times(1)).getUserId();
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByUserIdAndStatusIsIn(userId, assignmentStatuses);
        Mockito.verify(assignmentRepository, Mockito.times(1)).save(mockAssignmentEntity);
    }

    @Test
    void givenVoidRequest_whenAssignmentNotExistsForStarting_thenThrowsAysAssignmentUserNotStatusException() {

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
        Assertions.assertThrows(AysAssignmentUserNotStatusException.class, assignmentConcludeService::start);

        Mockito.verify(identity, Mockito.times(1)).getUserId();
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByUserIdAndStatusIsIn(userId, assignmentStatuses);
        Mockito.verify(assignmentRepository, Mockito.times(0)).save(mockAssignmentEntity);
    }

    @Test
    void givenVoidRequest_whenAssignmentReject_thenReturnVoid() {

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
        List<AssignmentStatus> assignmentStatuses = List.of(
                AssignmentStatus.RESERVED,
                AssignmentStatus.ASSIGNED,
                AssignmentStatus.IN_PROGRESS
        );

        Mockito.when(identity.getUserId()).thenReturn(userId);
        Mockito.when(identity.getInstitutionId()).thenReturn(institutionId);
        Mockito.when(assignmentRepository.findByUserIdAndStatusIsIn(userId, assignmentStatuses))
                .thenReturn(Optional.of(mockAssignmentEntity));
        Mockito.when(assignmentRepository.save(mockAssignmentEntity))
                .thenReturn(mockAssignmentEntity);
        Mockito.when(userRepository.save(mockUserEntity))
                .thenReturn(mockUserEntity);

        // Then
        assignmentConcludeService.reject();

        Mockito.verify(identity, Mockito.times(1)).getUserId();
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByUserIdAndStatusIsIn(userId, assignmentStatuses);
        Mockito.verify(assignmentRepository, Mockito.times(1)).save(mockAssignmentEntity);
        Mockito.verify(userRepository, Mockito.times(1)).save(mockUserEntity);
    }

    @Test
    void givenVoidRequest_whenAssignmentNotExistsForRejecting_thenThrowsAysAssignmentUserNotStatusException() {

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
        List<AssignmentStatus> assignmentStatuses = List.of(
                AssignmentStatus.RESERVED,
                AssignmentStatus.ASSIGNED,
                AssignmentStatus.IN_PROGRESS
        );

        Mockito.when(identity.getUserId()).thenReturn(userId);
        Mockito.when(identity.getInstitutionId()).thenReturn(institutionId);
        Mockito.when(assignmentRepository.findByUserIdAndStatusIsIn(userId, assignmentStatuses))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(AysAssignmentUserNotStatusException.class, assignmentConcludeService::reject);

        Mockito.verify(identity, Mockito.times(1)).getUserId();
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByUserIdAndStatusIsIn(userId, assignmentStatuses);
        Mockito.verify(assignmentRepository, Mockito.times(0)).save(mockAssignmentEntity);
    }

    @Test
    void givenVoidRequest_whenAssignmentComplete_thenReturnVoid() {

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
        List<AssignmentStatus> assignmentStatuses = List.of(AssignmentStatus.IN_PROGRESS);

        Mockito.when(identity.getUserId()).thenReturn(userId);
        Mockito.when(identity.getInstitutionId()).thenReturn(institutionId);
        Mockito.when(assignmentRepository.findByUserIdAndStatusIsIn(userId, assignmentStatuses))
                .thenReturn(Optional.of(mockAssignmentEntity));
        Mockito.when(assignmentRepository.save(mockAssignmentEntity))
                .thenReturn(mockAssignmentEntity);
        Mockito.when(userRepository.save(mockUserEntity))
                .thenReturn(mockUserEntity);

        // Then
        assignmentConcludeService.complete();

        Mockito.verify(identity, Mockito.times(1)).getUserId();
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByUserIdAndStatusIsIn(userId, assignmentStatuses);
        Mockito.verify(assignmentRepository, Mockito.times(1)).save(mockAssignmentEntity);
    }

    @Test
    void givenVoidRequest_whenAssignmentNotExistsForCompleting_thenThrowsAysAssignmentUserNotStatusException() {

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
        List<AssignmentStatus> assignmentStatuses = List.of(AssignmentStatus.IN_PROGRESS);

        Mockito.when(identity.getUserId()).thenReturn(userId);
        Mockito.when(identity.getInstitutionId()).thenReturn(institutionId);
        Mockito.when(assignmentRepository.findByUserIdAndStatusIsIn(userId, assignmentStatuses))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(AysAssignmentUserNotStatusException.class, assignmentConcludeService::complete);

        Mockito.verify(identity, Mockito.times(1)).getUserId();
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByUserIdAndStatusIsIn(userId, assignmentStatuses);
        Mockito.verify(assignmentRepository, Mockito.times(0)).save(mockAssignmentEntity);
    }

}
