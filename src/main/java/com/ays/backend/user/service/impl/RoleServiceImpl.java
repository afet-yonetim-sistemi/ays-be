package com.ays.backend.user.service.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.ays.backend.user.model.entities.Role;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.repository.RoleRepository;
import com.ays.backend.user.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public Optional<Role> findByName(UserRole name) {
        return roleRepository.findByName(name);
    }

    public Set<Role> getUserRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();
        if (strRoles != null) {
            Set<Role> set = new HashSet<>();
            for (String role : strRoles) {
                Role inputRole = switch (UserRole.getUserRoleByName(role)) {
                    case ROLE_ADMIN -> new Role(UserRole.ROLE_ADMIN);
                    case ROLE_WORKER -> new Role(UserRole.ROLE_WORKER);
                    case ROLE_VOLUNTEER -> new Role(UserRole.ROLE_VOLUNTEER);
                };
                set.add(inputRole);
            }
            roles = set;
        }

        return roles;
    }
}
