package org.ays.user.model.mapper;

import org.ays.common.model.mapper.BaseMapper;
import org.ays.user.model.User;
import org.ays.user.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * UserEntityToUserMapper is an interface that defines the mapping between an {@link UserEntity} and an {@link User}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface UserEntityToUserMapper extends BaseMapper<UserEntity, User> {

    @Override
    @Mapping(target = "phoneNumber.countryCode", source = "countryCode")
    @Mapping(target = "phoneNumber.lineNumber", source = "lineNumber")
    User map(UserEntity source);

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static UserEntityToUserMapper initialize() {
        return Mappers.getMapper(UserEntityToUserMapper.class);
    }

}
