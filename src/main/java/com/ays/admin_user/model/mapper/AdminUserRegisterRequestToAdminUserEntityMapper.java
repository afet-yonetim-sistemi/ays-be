package com.ays.admin_user.model.mapper;

import com.ays.admin_user.model.dto.request.AdminUserRegisterRequest;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.enums.AdminUserStatus;
import com.ays.common.model.mapper.BaseMapper;
import com.ays.common.util.AysRandomUtil;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * AdminUserRegisterRequestToAdminUserEntityMapper is an interface that defines the mapping between an {@link AdminUserRegisterRequest} and an {@link AdminUserEntity}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AdminUserRegisterRequestToAdminUserEntityMapper extends BaseMapper<AdminUserRegisterRequest, AdminUserEntity> {

    /**
     * Maps an {@link AdminUserRegisterRequest} object to an {@link AdminUserEntity} object for saving in the database.
     *
     * @param registerRequest the {@link AdminUserRegisterRequest} object to be mapped.
     * @param encodedPassword the {@link String} object.
     * @return the mapped {@link AdminUserEntity} object.
     */
    default AdminUserEntity mapForSaving(AdminUserRegisterRequest registerRequest, String encodedPassword) {
        return AdminUserEntity.builder()
                .id(AysRandomUtil.generateUUID())
                .institutionId(registerRequest.getInstitutionId())
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(encodedPassword)
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .countryCode(registerRequest.getPhoneNumber().getCountryCode().toString())
                .lineNumber(registerRequest.getPhoneNumber().getLineNumber().toString())
                .status(AdminUserStatus.NOT_VERIFIED)
                .build();
    }

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AdminUserRegisterRequestToAdminUserEntityMapper initialize() {
        return Mappers.getMapper(AdminUserRegisterRequestToAdminUserEntityMapper.class);
    }
}
