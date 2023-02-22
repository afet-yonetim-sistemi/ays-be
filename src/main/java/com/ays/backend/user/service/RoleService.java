package com.ays.backend.user.service;

import java.util.Optional;
import java.util.Set;

import com.ays.backend.user.model.entities.Role;
import com.ays.backend.user.model.enums.UserRole;


/**
 * Business logic service for user role operations
 */
public interface RoleService {
    /**
     * Finds a role by its name
     *
     * @param name name of the role
     * @return the role, if present, empty if not found.
     */
    Optional<Role> findByName(UserRole name);

    /**
     * Adds role to a user.
     *
     * @param roles set of roles
     * @return added roles to the user.
     */
    Set<Role> getUserRoles(Set<String> roles);
}
