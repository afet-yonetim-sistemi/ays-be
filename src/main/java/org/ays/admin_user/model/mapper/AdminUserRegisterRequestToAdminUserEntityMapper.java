package org.ays.admin_user.model.mapper;

import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCompleteRequest;
import org.ays.admin_user.model.entity.AdminUserEntity;
import org.ays.admin_user.model.enums.AdminRole;
import org.ays.admin_user.model.enums.AdminUserStatus;
import org.ays.common.model.mapper.BaseMapper;
import org.ays.common.util.AysRandomUtil;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * AdminUserRegisterRequestToAdminUserEntityMapper is an interface that defines the mapping between an {@link AdminUserRegisterApplicationCompleteRequest} and an {@link AdminUserEntity}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AdminUserRegisterRequestToAdminUserEntityMapper extends BaseMapper<AdminUserRegisterApplicationCompleteRequest, AdminUserEntity> {

    /**
     * Maps an {@link AdminUserRegisterApplicationCompleteRequest} object to an {@link AdminUserEntity} object for saving in the database.
     *
     * @param registerRequest the {@link AdminUserRegisterApplicationCompleteRequest} object to be mapped.
     * @param encodedPassword the {@link String} object.
     * @return the mapped {@link AdminUserEntity} object.
     */
    default AdminUserEntity mapForSaving(AdminUserRegisterApplicationCompleteRequest registerRequest, String encodedPassword) {
        return AdminUserEntity.builder()
                .id(AysRandomUtil.generateUUID())
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(encodedPassword)
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .countryCode(registerRequest.getPhoneNumber().getCountryCode())
                .lineNumber(registerRequest.getPhoneNumber().getLineNumber())
                .status(AdminUserStatus.NOT_VERIFIED)
                .role(AdminRole.ADMIN)
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
