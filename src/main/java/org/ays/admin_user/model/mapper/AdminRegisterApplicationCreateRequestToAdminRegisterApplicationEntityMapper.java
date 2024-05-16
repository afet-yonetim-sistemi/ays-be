package org.ays.admin_user.model.mapper;

import org.ays.admin_user.model.dto.request.AdminRegisterApplicationCreateRequest;
import org.ays.admin_user.model.entity.AdminRegisterApplicationEntity;
import org.ays.admin_user.model.enums.AdminRegisterApplicationStatus;
import org.ays.common.model.mapper.BaseMapper;
import org.ays.common.util.AysRandomUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * AdminUserRegisterApplicationCreateRequestToAdminUserRegisterApplicationEntityMapper is an interface that defines the
 * mapping between an {@link AdminRegisterApplicationCreateRequest} and an {@link AdminRegisterApplicationEntity}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AdminRegisterApplicationCreateRequestToAdminRegisterApplicationEntityMapper extends BaseMapper<AdminRegisterApplicationCreateRequest, AdminRegisterApplicationEntity> {

    /**
     * Maps an {@link AdminRegisterApplicationCreateRequest} object to an {@link AdminRegisterApplicationEntity}
     * object for saving in the database.
     *
     * @param registerRequest the {@link AdminRegisterApplicationCreateRequest} object to be mapped.
     * @return the mapped {@link AdminRegisterApplicationEntity} object.
     */
    @Named("mapForSaving")
    default AdminRegisterApplicationEntity mapForSaving(AdminRegisterApplicationCreateRequest registerRequest) {
        return AdminRegisterApplicationEntity.builder()
                .id(AysRandomUtil.generateUUID())
                .institutionId(registerRequest.getInstitutionId())
                .reason(registerRequest.getReason())
                .status(AdminRegisterApplicationStatus.WAITING)
                .build();
    }

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AdminRegisterApplicationCreateRequestToAdminRegisterApplicationEntityMapper initialize() {
        return Mappers.getMapper(AdminRegisterApplicationCreateRequestToAdminRegisterApplicationEntityMapper.class);
    }
}
