package org.ays.admin_user.model.mapper;

import org.ays.common.model.mapper.BaseMapper;
import org.ays.common.util.AysRandomUtil;
import org.ays.user.model.dto.request.AdminRegistrationApplicationCreateRequest;
import org.ays.user.model.entity.AdminRegistrationApplicationEntity;
import org.ays.user.model.enums.AdminRegistrationApplicationStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * AdminUserRegisterApplicationCreateRequestToAdminUserRegisterApplicationEntityMapper is an interface that defines the
 * mapping between an {@link AdminRegistrationApplicationCreateRequest} and an {@link AdminRegistrationApplicationEntity}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AdminRegistrationApplicationCreateRequestToAdminRegistrationApplicationEntityMapper extends BaseMapper<AdminRegistrationApplicationCreateRequest, AdminRegistrationApplicationEntity> {

    /**
     * Maps an {@link AdminRegistrationApplicationCreateRequest} object to an {@link AdminRegistrationApplicationEntity}
     * object for saving in the database.
     *
     * @param registerRequest the {@link AdminRegistrationApplicationCreateRequest} object to be mapped.
     * @return the mapped {@link AdminRegistrationApplicationEntity} object.
     */
    @Named("mapForSaving")
    default AdminRegistrationApplicationEntity mapForSaving(AdminRegistrationApplicationCreateRequest registerRequest) {
        return AdminRegistrationApplicationEntity.builder()
                .id(AysRandomUtil.generateUUID())
                .institutionId(registerRequest.getInstitutionId())
                .reason(registerRequest.getReason())
                .status(AdminRegistrationApplicationStatus.WAITING)
                .build();
    }

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AdminRegistrationApplicationCreateRequestToAdminRegistrationApplicationEntityMapper initialize() {
        return Mappers.getMapper(AdminRegistrationApplicationCreateRequestToAdminRegistrationApplicationEntityMapper.class);
    }
}
