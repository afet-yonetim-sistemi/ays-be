package com.ays.backend.user.model.entities;

import com.ays.backend.user.model.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;

/**
 * User types entity, which can be assigned to the users.
 */
@Entity
@Table(name = "user_types")
@RequiredArgsConstructor
public class UserType extends BaseEntity {

    @Enumerated(EnumType.ORDINAL)
    @Column(length = 30, unique = true, nullable = false)
    private UserRole description;
}