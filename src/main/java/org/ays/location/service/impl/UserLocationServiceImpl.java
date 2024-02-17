package org.ays.location.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.assignment.model.enums.AssignmentStatus;
import org.ays.assignment.repository.AssignmentRepository;
import org.ays.auth.model.AysIdentity;
import org.ays.location.model.dto.request.UserLocationSaveRequest;
import org.ays.location.model.entity.UserLocationEntity;
import org.ays.location.model.mapper.UserLocationSaveRequestToUserLocationEntityMapper;
import org.ays.location.repository.UserLocationRepository;
import org.ays.location.service.UserLocationService;
import org.ays.location.util.exception.AysUserLocationCannotBeUpdatedException;
import org.springframework.stereotype.Service;

/**
 * Implementation of the UserLocationService interface that manages and stores user location data.
 * This service utilizes a repository to interact with the database for saving user location information.
 */
@Service
@RequiredArgsConstructor
class UserLocationServiceImpl implements UserLocationService {

    private final UserLocationRepository userLocationRepository;
    private final AssignmentRepository assignmentRepository;

    private final AysIdentity identity;


    private final UserLocationSaveRequestToUserLocationEntityMapper userLocationSaveRequestToUserLocationEntityMapper = UserLocationSaveRequestToUserLocationEntityMapper.initialize();

    /**
     * Saves the user's location based on the provided UserLocationSaveRequest.
     * If the user's location already exists in the database, updates the location; otherwise, creates a new entry.
     *
     * @param saveRequest The request containing the user's location information to be saved.
     */
    @Override
    public void saveUserLocation(final UserLocationSaveRequest saveRequest) {

        final String userId = identity.getUserId();
        final boolean isAssignmentInProgress = assignmentRepository
                .existsByUserIdAndStatus(userId, AssignmentStatus.IN_PROGRESS);
        if (!isAssignmentInProgress) {
            throw new AysUserLocationCannotBeUpdatedException(userId);
        }

        userLocationRepository.findByUserId(userId)
                .ifPresentOrElse(
                        userLocationEntityFromDatabase -> {
                            userLocationEntityFromDatabase.setPoint(saveRequest.getLongitude(), saveRequest.getLatitude());
                            userLocationRepository.save(userLocationEntityFromDatabase);
                        },
                        () -> {
                            final UserLocationEntity userLocationEntity = userLocationSaveRequestToUserLocationEntityMapper
                                    .mapForSaving(saveRequest, userId);
                            userLocationRepository.save(userLocationEntity);
                        }
                );
    }

}
