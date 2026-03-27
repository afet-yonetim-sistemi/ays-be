package org.ays.auth.model.mapper;

import org.ays.auth.model.AysUser;
import org.ays.auth.model.entity.AysUserEntity;
import org.ays.common.model.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * {@link AysUserLoginAttemptToEntityMapper} maps {@link AysUser.LoginAttempt} domain models
 * to {@link AysUserEntity.LoginAttemptEntity} persistence models.
 */
@Mapper
public interface AysUserLoginAttemptToEntityMapper extends BaseMapper<AysUser.LoginAttempt, AysUserEntity.LoginAttemptEntity> {

    static AysUserLoginAttemptToEntityMapper initialize() {
        return Mappers.getMapper(AysUserLoginAttemptToEntityMapper.class);
    }

}
