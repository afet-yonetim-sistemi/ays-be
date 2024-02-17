package org.ays.location.service.impl;

import org.ays.AbstractUnitTest;
import org.ays.assignment.model.enums.AssignmentStatus;
import org.ays.assignment.repository.AssignmentRepository;
import org.ays.auth.model.AysIdentity;
import org.ays.common.util.AysRandomUtil;
import org.ays.location.model.dto.request.UserLocationSaveRequest;
import org.ays.location.model.dto.request.UserLocationSaveRequestBuilder;
import org.ays.location.model.entity.UserLocationEntity;
import org.ays.location.model.mapper.UserLocationSaveRequestToUserLocationEntityMapper;
import org.ays.location.repository.UserLocationRepository;
import org.ays.location.util.exception.AysUserLocationCannotBeUpdatedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

class UserLocationServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private UserLocationServiceImpl userLocationService;

    @Mock
    private UserLocationRepository userLocationRepository;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private AysIdentity identity;


    private final UserLocationSaveRequestToUserLocationEntityMapper userLocationSaveRequestToUserLocationEntityMapper = UserLocationSaveRequestToUserLocationEntityMapper.initialize();

    @Test
    void givenValidUserLocationSaveRequest_whenUserLocationNotFound_thenSaveUserLastLocation() {
        // Given
        UserLocationSaveRequest mockSaveRequest = new UserLocationSaveRequestBuilder()
                .withValidFields()
                .build();
        String userId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(identity.getUserId()).thenReturn(userId);

        Mockito.when(assignmentRepository.existsByUserIdAndStatus(userId, AssignmentStatus.IN_PROGRESS))
                .thenReturn(true);

        UserLocationEntity userLocationEntity = userLocationSaveRequestToUserLocationEntityMapper
                .mapForSaving(mockSaveRequest, userId);
        Mockito.when(userLocationRepository.findByUserId(userId))
                .thenReturn(Optional.of(userLocationEntity));

        // Then
        userLocationService.saveUserLocation(mockSaveRequest);

        // Verify
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .existsByUserIdAndStatus(Mockito.anyString(), Mockito.any(AssignmentStatus.class));
        Mockito.verify(userLocationRepository, Mockito.times(1))
                .findByUserId(Mockito.anyString());
        Mockito.verify(userLocationRepository, Mockito.times(1))
                .save(Mockito.any(UserLocationEntity.class));
    }

    @Test
    void givenValidUserLocationSaveRequest_whenAssignmentNotFoundOrNotInProgress_thenThrowAysUserLocationCannotBeUpdatedException() {
        // Given
        UserLocationSaveRequest mockSaveRequest = new UserLocationSaveRequestBuilder()
                .withValidFields()
                .build();
        String userId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(identity.getUserId()).thenReturn(userId);

        Mockito.when(assignmentRepository.existsByUserIdAndStatus(userId, AssignmentStatus.IN_PROGRESS))
                .thenReturn(false);

        // Then
        Assertions.assertThrows(
                AysUserLocationCannotBeUpdatedException.class,
                () -> userLocationService.saveUserLocation(mockSaveRequest)
        );

        // Verify
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .existsByUserIdAndStatus(Mockito.anyString(), Mockito.any(AssignmentStatus.class));
        Mockito.verify(userLocationRepository, Mockito.times(0))
                .findByUserId(Mockito.anyString());
        Mockito.verify(userLocationRepository, Mockito.times(0))
                .save(Mockito.any(UserLocationEntity.class));
    }

    @Test
    void givenValidUserLocationSaveRequest_whenUserLocationNotFound_thenUpdateUserLastLocation() {
        // Given
        UserLocationSaveRequest mockSaveRequest = new UserLocationSaveRequestBuilder()
                .withValidFields()
                .build();
        String userId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(identity.getUserId()).thenReturn(userId);

        Mockito.when(assignmentRepository.existsByUserIdAndStatus(userId, AssignmentStatus.IN_PROGRESS))
                .thenReturn(true);

        // Then
        userLocationService.saveUserLocation(mockSaveRequest);

        // Verify
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .existsByUserIdAndStatus(Mockito.anyString(), Mockito.any(AssignmentStatus.class));
        Mockito.verify(userLocationRepository, Mockito.times(1))
                .findByUserId(Mockito.anyString());
        Mockito.verify(userLocationRepository, Mockito.times(1))
                .save(Mockito.any(UserLocationEntity.class));
    }

}