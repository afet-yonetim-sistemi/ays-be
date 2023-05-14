package com.ays.user.model.mapper;

import com.ays.common.model.mapper.BaseMapper;
import com.ays.common.util.AysRandomUtil;
import com.ays.user.model.User;
import com.ays.user.model.dto.request.UserSaveRequest;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * UserToUserResponseMapper is an interface that defines the mapping between an {@link UserSaveRequest} and an {@link User}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface UserSaveRequestToUserMapper extends BaseMapper<UserSaveRequest, User> {

    /**
     * Maps an {@link UserSaveRequest} object to an {@link UserEntity} object for saving in the database.
     *
     * @param saveRequest     the {@link UserSaveRequest} object to be mapped.
     * @param organizationId  the {@link String} object.
     * @param username        the {@link String} object.
     * @param encodedPassword the {@link String} object.
     * @return the mapped {@link UserEntity} object.
     */
    default UserEntity mapForSaving(UserSaveRequest saveRequest,
                                    String organizationId,
                                    String username,
                                    String encodedPassword) {

        return UserEntity.builder()
                .id(AysRandomUtil.generateUUID())
                .organizationId(organizationId)
                .username(username)
                .password(encodedPassword)
                .firstName(saveRequest.getFirstName())
                .lastName(saveRequest.getLastName())
                .countryCode(saveRequest.getPhoneNumber().getCountryCode())
                .lineNumber(saveRequest.getPhoneNumber().getLineNumber())
                .role(UserRole.VOLUNTEER)
                .status(UserStatus.ACTIVE)
                .build();
    }

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static UserSaveRequestToUserMapper initialize() {
        return Mappers.getMapper(UserSaveRequestToUserMapper.class);
    }

}
