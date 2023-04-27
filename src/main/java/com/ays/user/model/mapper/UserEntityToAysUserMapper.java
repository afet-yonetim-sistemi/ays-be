package com.ays.user.model.mapper;

import com.ays.auth.model.AysUser;
import com.ays.common.model.mapper.BaseMapper;
import com.ays.user.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * UserEntityToAysUserMapper is an interface that defines the mapping between a {@link UserEntity} and an {@link AysUser}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface UserEntityToAysUserMapper extends BaseMapper<UserEntity, AysUser> {

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static UserEntityToAysUserMapper initialize() {
        return Mappers.getMapper(UserEntityToAysUserMapper.class);
    }

}
