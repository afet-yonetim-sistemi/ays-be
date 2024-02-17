package org.ays.assignment.service.impl.handler;

import org.ays.AbstractUnitTest;
import org.ays.assignment.model.entity.AssignmentEntity;
import org.ays.assignment.model.enums.AssignmentStatus;
import org.ays.assignment.repository.AssignmentRepository;
import org.ays.assignment.util.exception.AysAssignmentNotExistByUserIdAndStatusException;
import org.ays.auth.model.AysIdentity;
import org.ays.common.util.AysRandomUtil;
import org.ays.user.model.entity.UserEntity;
import org.ays.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.EnumSet;
import java.util.Optional;

class AssignmentCancelHandlerTest extends AbstractUnitTest {

    @InjectMocks
    private AssignmentCancelHandler assignmentCancelHandler;

    @Mock
    private AysIdentity identity;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void whenAssignmentCanceled_thenReturnNothing() {

        // Given
        final String userId = AysRandomUtil.generateUUID();
        final AssignmentEntity mockAssignmentEntity = Mockito.mock(AssignmentEntity.class);
        final UserEntity mockUserEntity = Mockito.mock(UserEntity.class);

        // When
        Mockito.when(identity.getUserId()).thenReturn(userId);
        final EnumSet<AssignmentStatus> acceptedStatuses = EnumSet.of(AssignmentStatus.ASSIGNED, AssignmentStatus.IN_PROGRESS);
        Mockito.when(assignmentRepository.findByUserIdAndStatusIn(userId, acceptedStatuses))
                .thenReturn(Optional.of(mockAssignmentEntity));
        Mockito.when(mockAssignmentEntity.getUser()).thenReturn(mockUserEntity);
        Mockito.when(userRepository.save(mockUserEntity)).thenReturn(mockUserEntity);
        Mockito.when(assignmentRepository.save(mockAssignmentEntity)).thenReturn(mockAssignmentEntity);

        // Then
        assignmentCancelHandler.handle();

        Mockito.verify(identity, Mockito.times(1)).getUserId();
        Mockito.verify(assignmentRepository, Mockito.times(1)).findByUserIdAndStatusIn(userId, acceptedStatuses);
        Mockito.verify(mockUserEntity, Mockito.times(1)).busy();
        Mockito.verify(userRepository, Mockito.times(1)).save(mockUserEntity);
        Mockito.verify(mockAssignmentEntity, Mockito.times(1)).available();
        Mockito.verify(assignmentRepository, Mockito.times(1)).save(mockAssignmentEntity);
    }

    @Test
    void whenAssignmentNotExistsForCanceling_thenThrowAysAssignmentNotExistByUserIdAndStatusException() {

        // Given
        final String userId = AysRandomUtil.generateUUID();
        final AssignmentEntity mockAssignmentEntity = Mockito.mock(AssignmentEntity.class);
        final UserEntity mockUserEntity = Mockito.mock(UserEntity.class);

        // When
        Mockito.when(identity.getUserId()).thenReturn(userId);
        final EnumSet<AssignmentStatus> acceptedStatuses = EnumSet.of(AssignmentStatus.ASSIGNED, AssignmentStatus.IN_PROGRESS);
        Mockito.when(assignmentRepository.findByUserIdAndStatusIn(userId, acceptedStatuses))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAssignmentNotExistByUserIdAndStatusException.class,
                () -> assignmentCancelHandler.handle()
        );

        Mockito.verify(identity, Mockito.times(1)).getUserId();
        Mockito.verify(assignmentRepository, Mockito.times(1)).findByUserIdAndStatusIn(userId, acceptedStatuses);
        Mockito.verify(mockUserEntity, Mockito.never()).busy();
        Mockito.verify(userRepository, Mockito.never()).save(mockUserEntity);
        Mockito.verify(mockAssignmentEntity, Mockito.never()).available();
        Mockito.verify(assignmentRepository, Mockito.never()).save(mockAssignmentEntity);
    }
}
