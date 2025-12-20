package org.ays.auth.service;

import org.ays.auth.model.request.AysMobileUserRegisterRequest;

/**
 * Service interface for mobile user registration.
 */
public interface AysMobileUserRegisterService {

    /**
     * Registers a new mobile user.
     * Creates a new user with NOT_VERIFIED status.
     *
     * @param registerRequest The registration request containing user details.
     */
    void register(AysMobileUserRegisterRequest registerRequest);

}
