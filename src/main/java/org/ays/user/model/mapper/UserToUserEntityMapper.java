package org.ays.user.model.mapper;

import org.ays.common.model.mapper.BaseMapper;
import org.ays.user.model.User;
import org.ays.user.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * {@link UserToUserEntityMapper} is an interface that defines the mapping between an {@link User} and an {@link UserEntity}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface UserToUserEntityMapper extends BaseMapper<User, UserEntity> {

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static UserToUserEntityMapper initialize() {
        return Mappers.getMapper(UserToUserEntityMapper.class);
    }
}
