package org.ays.common.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Optional;

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

    @Column(name = "CREATED_USER")
    protected String createdUser;

    @Column(name = "CREATED_AT")
    protected LocalDateTime createdAt;

    @PrePersist
    public void prePersist() { // TODO : Auth V2 Production'a alınınca buradaki yorum satırı kaldırılacak.
//        this.createdUser = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
//                .map(Authentication::getPrincipal)
//                .filter(user -> !"anonymousUser".equals(user))
//                .map(Jwt.class::cast)
//                .map(jwt -> jwt.getClaim(AysTokenClaims.USER_EMAIL_ADDRESS.getValue()).toString())
//                .orElse("AYS");
        this.createdUser = "AYS";
        this.createdAt = Optional.ofNullable(this.createdAt)
                .orElse(LocalDateTime.now());
    }


    @Column(name = "UPDATED_USER")
    protected String updatedUser;

    @Column(name = "UPDATED_AT")
    protected LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() { // TODO : Auth V2 Production'a alınınca buradaki yorum satırı kaldırılacak.
//        this.updatedUser = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
//                .map(Authentication::getPrincipal)
//                .filter(user -> !"anonymousUser".equals(user))
//                .map(Jwt.class::cast)
//                .map(jwt -> jwt.getClaim(AysTokenClaims.USER_EMAIL_ADDRESS.getValue()).toString())
//                .orElse("AYS");
        this.createdUser = "AYS";
        this.updatedAt = LocalDateTime.now();
    }
}
