package com.ays.common.model.entity;

import com.ays.auth.model.enums.AysTokenClaims;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDateTime;

/**
 * Base entity to be used in order to pass the common fields to the entities in the same module.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "CREATED_USER")
    protected String createdUser;

    @Column(name = "CREATED_AT")
    protected LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdUser = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getClaim(AysTokenClaims.USERNAME.getValue());
        this.createdAt = LocalDateTime.now();
    }


    @Column(name = "UPDATED_USER")
    protected String updatedUser;

    @Column(name = "UPDATED_AT")
    protected LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedUser = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getClaim(AysTokenClaims.USERNAME.getValue());
        this.updatedAt = LocalDateTime.now();
    }
}
