package com.ays.admin_user.model.mapper;

import com.ays.admin_user.model.dto.request.AdminUserRegisterRequest;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.enums.AdminUserStatus;
import com.ays.common.model.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Mapper
public interface AdminUserRegisterRequestToAdminUserEntityMapper extends BaseMapper<AdminUserRegisterRequest, AdminUserEntity> {

    default AdminUserEntity mapForSaving(AdminUserRegisterRequest registerRequest, PasswordEncoder passwordEncoder) {
        return AdminUserEntity.builder()
                .id(UUID.randomUUID().toString())
                .organizationId(registerRequest.getOrganizationId())
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .countryCode(registerRequest.getPhoneNumber().getCountryCode())
                .lineNumber(registerRequest.getPhoneNumber().getLineNumber())
                .status(AdminUserStatus.NOT_VERIFIED)
                .build();
    }

    static AdminUserRegisterRequestToAdminUserEntityMapper initialize() {
        return Mappers.getMapper(AdminUserRegisterRequestToAdminUserEntityMapper.class);
    }
}
