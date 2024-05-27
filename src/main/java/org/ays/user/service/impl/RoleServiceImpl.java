package org.ays.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysIdentity;
import org.ays.user.model.dto.request.RoleCreateRequest;
import org.ays.user.model.entity.PermissionEntity;
import org.ays.user.model.entity.RoleEntity;
import org.ays.user.repository.PermissionRepository;
import org.ays.user.repository.RoleRepository;
import org.ays.user.service.RoleService;
import org.ays.user.util.exception.AysPermissionNotExistException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {


    private final AysIdentity identity;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public void createRole(RoleCreateRequest roleCreateRequest) {

        final Set<String> permissionIds = roleCreateRequest.getPermissionIds();
        final Set<PermissionEntity> permissionEntities = permissionRepository.findByIdIn(permissionIds);

        if (permissionEntities.size() != permissionIds.size()) {

            Set<String> foundIds = permissionEntities.stream()
                    .map(PermissionEntity::getId)
                    .collect(Collectors.toSet());
            Set<String> notFoundIds = new HashSet<>(permissionIds);
            notFoundIds.removeAll(foundIds);

            throw new AysPermissionNotExistException(notFoundIds+""); // ??? Bunu sor
        }
        // TO DO : Dokümanı sor neden requestte saçma veriler var??
        final RoleEntity roleEntity = RoleEntity.builder()
                .name(roleCreateRequest.getName())
                .institutionId(identity.getInstitutionId())
                .permissions(permissionEntities)
                .build();

         roleRepository.save(roleEntity);

    }
}
