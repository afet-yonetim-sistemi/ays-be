package com.ays.location.service;

import com.ays.location.model.dto.request.UserLocationSaveRequest;

/**
 * The UserLocationService interface provides methods to manage and store user location data.
 * Implementing classes should define the behavior to save user location information.
 */
public interface UserLocationService {

    /**
     * Saves the user's location based on the provided UserLocationSaveRequest.
     *
     * @param saveRequest The request containing the user's location information to be saved.
     */
    void saveUserLocation(UserLocationSaveRequest saveRequest);

}
