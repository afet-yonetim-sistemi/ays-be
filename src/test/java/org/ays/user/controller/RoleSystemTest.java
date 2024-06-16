package org.ays.user.controller;

import org.ays.AbstractSystemTest;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.user.model.dto.request.RoleCreateRequestBuilder;
import org.ays.user.model.entity.PermissionEntity;
import org.ays.user.model.entity.RoleEntity;
import org.ays.user.model.enums.RoleStatus;
import org.ays.user.model.request.RoleCreateRequest;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class RoleSystemTest extends AbstractSystemTest {

    private static final String BASE_PATH = "/api/v1";

    @Test
    @Transactional
    void givenValidRoleCreateRequest_whenSuperRoleCreated_thenReturnSuccess() throws Exception {

        // Initialize
        Set<PermissionEntity> permissionEntities = new HashSet<>(permissionRepository.findAll());
        Set<String> permissionIds = permissionEntities.stream()
                .map(PermissionEntity::getId)
                .collect(Collectors.toSet());

        // Given
        RoleCreateRequest createRequest = new RoleCreateRequestBuilder()
                .withPermissionIds(permissionIds)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminTokenV2.getAccessToken(), createRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<RoleEntity> roleEntity = roleRepository.findByName(createRequest.getName());

        Assertions.assertTrue(roleEntity.isPresent());
        Assertions.assertNotNull(roleEntity.get().getId());
        Assertions.assertNotNull(roleEntity.get().getInstitutionId());
        Assertions.assertEquals(createRequest.getName(), roleEntity.get().getName());
        Assertions.assertEquals(RoleStatus.ACTIVE, roleEntity.get().getStatus());
        createRequest.getPermissionIds().forEach(permissionId -> {
            Assertions.assertTrue(
                    roleEntity.get().getPermissions().stream()
                            .anyMatch(permission -> permission.getId().equals(permissionId))
            );
        });
    }

    @Test
    @Transactional
    void givenValidRoleCreateRequest_whenRoleCreated_thenReturnSuccess() throws Exception {

        // Initialize
        Set<PermissionEntity> permissionEntities = permissionRepository.findAllByIsSuperFalse();
        Set<String> permissionIds = permissionEntities.stream()
                .map(PermissionEntity::getId)
                .collect(Collectors.toSet());

        // Given
        RoleCreateRequest createRequest = new RoleCreateRequestBuilder()
                .withPermissionIds(permissionIds)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, adminTokenV2.getAccessToken(), createRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<RoleEntity> roleEntity = roleRepository.findByName(createRequest.getName());

        Assertions.assertTrue(roleEntity.isPresent());
        Assertions.assertNotNull(roleEntity.get().getId());
        Assertions.assertNotNull(roleEntity.get().getInstitutionId());
        Assertions.assertEquals(createRequest.getName(), roleEntity.get().getName());
        Assertions.assertEquals(RoleStatus.ACTIVE, roleEntity.get().getStatus());
        createRequest.getPermissionIds().forEach(permissionId -> {
            Assertions.assertTrue(
                    roleEntity.get().getPermissions().stream()
                            .anyMatch(permission -> permission.getId().equals(permissionId))
            );
        });
    }

}
