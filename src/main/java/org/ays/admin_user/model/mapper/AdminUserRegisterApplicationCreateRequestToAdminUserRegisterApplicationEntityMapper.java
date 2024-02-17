package org.ays.admin_user.model.mapper;

import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCreateRequest;
import org.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import org.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import org.ays.common.model.mapper.BaseMapper;
import org.ays.common.util.AysRandomUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * AdminUserRegisterApplicationCreateRequestToAdminUserRegisterApplicationEntityMapper is an interface that defines the
 * mapping between an {@link AdminUserRegisterApplicationCreateRequest} and an {@link AdminUserRegisterApplicationEntity}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AdminUserRegisterApplicationCreateRequestToAdminUserRegisterApplicationEntityMapper extends BaseMapper<AdminUserRegisterApplicationCreateRequest, AdminUserRegisterApplicationEntity> {

    /**
     * Maps an {@link AdminUserRegisterApplicationCreateRequest} object to an {@link AdminUserRegisterApplicationEntity}
     * object for saving in the database.
     *
     * @param registerRequest the {@link AdminUserRegisterApplicationCreateRequest} object to be mapped.
     * @return the mapped {@link AdminUserRegisterApplicationEntity} object.
     */
    @Named("mapForSaving")
    default AdminUserRegisterApplicationEntity mapForSaving(AdminUserRegisterApplicationCreateRequest registerRequest) {
        return AdminUserRegisterApplicationEntity.builder()
                .id(AysRandomUtil.generateUUID())
                .institutionId(registerRequest.getInstitutionId())
                .reason(registerRequest.getReason())
                .status(AdminUserRegisterApplicationStatus.WAITING)
                .build();
    }

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AdminUserRegisterApplicationCreateRequestToAdminUserRegisterApplicationEntityMapper initialize() {
        return Mappers.getMapper(AdminUserRegisterApplicationCreateRequestToAdminUserRegisterApplicationEntityMapper.class);
    }
}
