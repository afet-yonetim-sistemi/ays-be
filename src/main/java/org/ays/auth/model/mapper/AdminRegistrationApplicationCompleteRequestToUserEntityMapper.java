package org.ays.auth.model.mapper;

import org.ays.auth.model.entity.UserEntityV2;
import org.ays.auth.model.enums.UserStatus;
import org.ays.auth.model.request.AdminRegistrationApplicationCompleteRequest;
import org.ays.common.model.mapper.BaseMapper;

/**
 * {@link AdminRegistrationApplicationCompleteRequestToUserEntityMapper} is an interface that defines the mapping between an {@link AdminRegistrationApplicationCompleteRequest} and an {@link UserEntityV2}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
public interface AdminRegistrationApplicationCompleteRequestToUserEntityMapper {

    /**
     * Maps an {@link AdminRegistrationApplicationCompleteRequest} object to an {@link UserEntityV2} object for saving in the database.
     *
     * @param registerRequest the {@link AdminRegistrationApplicationCompleteRequest} object to be mapped.
     * @return the mapped {@link UserEntityV2} object.
     */
    default UserEntityV2.UserEntityV2Builder mapForSaving(final AdminRegistrationApplicationCompleteRequest registerRequest) {

        return UserEntityV2.builder()
                .emailAddress(registerRequest.getEmailAddress())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .countryCode(registerRequest.getPhoneNumber().getCountryCode())
                .lineNumber(registerRequest.getPhoneNumber().getLineNumber())
                .city(registerRequest.getCity())
                .status(UserStatus.NOT_VERIFIED);
    }

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AdminRegistrationApplicationCompleteRequestToUserEntityMapper initialize() {
        return new AdminRegistrationApplicationCompleteRequestToUserEntityMapper() {
            @Override
            public UserEntityV2.UserEntityV2Builder mapForSaving(AdminRegistrationApplicationCompleteRequest registerRequest) {
                return AdminRegistrationApplicationCompleteRequestToUserEntityMapper.super.mapForSaving(registerRequest);
            }
        };
    }

}
