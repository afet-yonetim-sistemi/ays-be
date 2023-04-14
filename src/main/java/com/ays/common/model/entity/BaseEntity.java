package com.ays.common.model.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Base entity to be used in order to pass the common fields to the entities in the same module.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    protected String createdUser;

    protected LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdUser = "AYS";
        this.createdAt = LocalDateTime.now();
    }


    protected LocalDateTime updatedAt;

    protected String updatedUser;

    @PreUpdate
    public void preUpdate() {
        this.updatedUser = "AYS";
        this.updatedAt = LocalDateTime.now();
    }
}
