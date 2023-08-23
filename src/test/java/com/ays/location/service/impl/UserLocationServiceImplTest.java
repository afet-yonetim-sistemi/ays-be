package com.ays.location.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.auth.model.AysIdentity;
import com.ays.common.util.AysRandomUtil;
import com.ays.location.model.dto.request.UserLocationSaveRequest;
import com.ays.location.model.dto.request.UserLocationSaveRequestBuilder;
import com.ays.location.model.entity.UserLocationEntity;
import com.ays.location.model.mapper.UserLocationSaveRequestToUserLocationEntityMapper;
import com.ays.location.repository.UserLocationRepository;
import com.ays.location.util.exception.AysUserLocationCannotBeUpdatedException;
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


    private static final UserLocationSaveRequestToUserLocationEntityMapper USER_LOCATION_SAVE_REQUEST_TO_USER_LOCATION_ENTITY_MAPPER = UserLocationSaveRequestToUserLocationEntityMapper.initialize();

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

        UserLocationEntity userLocationEntity = USER_LOCATION_SAVE_REQUEST_TO_USER_LOCATION_ENTITY_MAPPER
                .mapForSaving(mockSaveRequest, userId);
        Mockito.when(userLocationRepository.findByUserId(userId))
                .thenReturn(Optional.of(userLocationEntity));

        // Then
        userLocationService.saveUserLocation(mockSaveRequest);

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

        Mockito.verify(assignmentRepository, Mockito.times(1))
                .existsByUserIdAndStatus(Mockito.anyString(), Mockito.any(AssignmentStatus.class));
        Mockito.verify(userLocationRepository, Mockito.times(1))
                .findByUserId(Mockito.anyString());
        Mockito.verify(userLocationRepository, Mockito.times(1))
                .save(Mockito.any(UserLocationEntity.class));
    }

}