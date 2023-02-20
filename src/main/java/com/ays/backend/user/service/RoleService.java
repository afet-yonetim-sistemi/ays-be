package com.ays.backend.user.service;

import com.ays.backend.user.model.entities.Role;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Optional<Role> findByName(UserRole name) {
        return roleRepository.findByName(name);
    }

    public Set<Role> addRoleToUser(Set<String> strRoles ) {

        Set<Role> roles = new HashSet<>();

        if (strRoles != null) {
            roles = strRoles.stream()
                    .map(role -> {
                        switch (role) {
                            case "ROLE_ADMIN":
                                return roleRepository.findByName(UserRole.ROLE_ADMIN)
                                        .orElseGet(() -> new Role(UserRole.ROLE_ADMIN));
                            case "ROLE_WORKER":
                                return roleRepository.findByName(UserRole.ROLE_WORKER)
                                        .orElseGet(() -> new Role(UserRole.ROLE_WORKER));
                            default:
                                return roleRepository.findByName(UserRole.ROLE_VOLUNTEER)
                                        .orElseGet(() -> new Role(UserRole.ROLE_VOLUNTEER));
                        }
                    })
                    .collect(Collectors.toSet());
        } else {
            Set<Role> finalRoles = roles;
            roleRepository.findByName(UserRole.ROLE_VOLUNTEER)
                    .ifPresentOrElse(roles::add, () -> finalRoles.add(new Role(UserRole.ROLE_VOLUNTEER)));
        }

        saveRoles(roles);

        return roles;

    }

    public void saveRoles(Set<Role> roles){
        roleRepository.saveAll(roles);
    }
}
