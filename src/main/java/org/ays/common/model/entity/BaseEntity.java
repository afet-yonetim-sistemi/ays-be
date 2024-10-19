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
import org.ays.auth.model.enums.AysTokenClaims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Abstract base class for entities that provides auditing fields and lifecycle hooks.
 * <p>
 * The {@link BaseEntity} class defines common fields and behavior for auditing purposes
 * in entities that extend it. It includes fields for tracking the user who created and
 * updated the entity, as well as timestamps for when the entity was created and last updated.
 * The fields are automatically populated during entity persistence and update operations.
 * </p>
 *
 * <h3>Example Usage</h3>
 * <pre>{@code
 * @SuperBuilder
 * @Entity
 * public class SomeEntity extends BaseEntity {
 *     // additional fields and methods
 * }
 * }</pre>
 *
 * <p>
 * This class utilizes Spring Security's {@link SecurityContextHolder} to fetch the currently
 * authenticated user's id and sets it as the {@code createdUser} or {@code updatedUser}.
 * It also sets the timestamps {@code createdAt} and {@code updatedAt}.
 * </p>
 *
 * <pre>
 * Note: Ensure that Spring Security's context is properly configured to use JWTs and that the
 * principal contains a claim for the user's id.
 * </pre>
 *
 * @see SecurityContextHolder
 * @see Jwt
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
    public void prePersist() {
        this.createdUser = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(user -> !"anonymousUser".equals(user))
                .map(Jwt.class::cast)
                .map(jwt -> jwt.getClaim(AysTokenClaims.USER_ID.getValue()).toString())
                .orElse("AYS");
        this.createdAt = Optional.ofNullable(this.createdAt)
                .orElse(LocalDateTime.now());
    }


    @Column(name = "UPDATED_USER")
    protected String updatedUser;

    @Column(name = "UPDATED_AT")
    protected LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedUser = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(user -> !"anonymousUser".equals(user))
                .map(Jwt.class::cast)
                .map(jwt -> jwt.getClaim(AysTokenClaims.USER_ID.getValue()).toString())
                .orElse("AYS");
        this.updatedAt = LocalDateTime.now();
    }

}
