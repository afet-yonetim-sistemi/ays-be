package com.ays.backend.user.repository;

import com.ays.backend.user.model.UserRole;
import com.ays.backend.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(UserRole name);
}
