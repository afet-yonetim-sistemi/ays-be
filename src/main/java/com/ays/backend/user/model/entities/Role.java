package com.ays.backend.user.model.entities;

import com.ays.backend.user.model.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Roles entity, which can be assigned to the users.
 */
@Entity
@Table(name = "roles")
@Data
@RequiredArgsConstructor
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(length = 30, unique = true, nullable = false)
    private UserRole name;

    public Role(UserRole name) {
        this.name = name;
    }

}
