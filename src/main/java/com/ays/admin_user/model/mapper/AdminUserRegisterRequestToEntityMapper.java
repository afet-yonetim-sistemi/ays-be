package com.ays.admin_user.model.mapper;

import com.ays.admin_user.controller.dto.request.AdminUserRegisterRequest;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.enums.AdminUserStatus;
import com.ays.common.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Mapper
public interface AdminUserRegisterRequestToEntityMapper extends BaseMapper<AdminUserRegisterRequest, AdminUserEntity> {

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

    static AdminUserRegisterRequestToEntityMapper initialize() {
        return Mappers.getMapper(AdminUserRegisterRequestToEntityMapper.class);
    }
}
