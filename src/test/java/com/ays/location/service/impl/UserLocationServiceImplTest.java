package com.ays.location.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.auth.model.AysIdentity;
import com.ays.common.util.AysRandomUtil;
import com.ays.location.model.dto.request.UserLocationSaveRequest;
import com.ays.location.model.dto.request.UserLocationSaveRequestBuilder;
import com.ays.location.model.entity.UserLocationEntity;
import com.ays.location.model.mapper.UserLocationSaveRequestToUserLocationEntityMapper;
import com.ays.location.repository.UserLocationRepository;
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

        UserLocationEntity userLocationEntity = USER_LOCATION_SAVE_REQUEST_TO_USER_LOCATION_ENTITY_MAPPER
                .mapForSaving(mockSaveRequest, userId);
        Mockito.when(userLocationRepository.findByUserId(userId))
                .thenReturn(Optional.of(userLocationEntity));

        // Then
        userLocationService.saveUserLocation(mockSaveRequest);

        Mockito.verify(userLocationRepository, Mockito.times(1))
                .findByUserId(Mockito.anyString());
        Mockito.verify(userLocationRepository, Mockito.times(1))
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

        // Then
        userLocationService.saveUserLocation(mockSaveRequest);

        Mockito.verify(userLocationRepository, Mockito.times(1))
                .findByUserId(Mockito.anyString());
        Mockito.verify(userLocationRepository, Mockito.times(1))
                .save(Mockito.any(UserLocationEntity.class));
    }

}