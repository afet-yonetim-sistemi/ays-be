package com.ays.backend.user.repository;

import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.model.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(UserRole name);
}
