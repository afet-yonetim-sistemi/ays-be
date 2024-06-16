package org.ays.auth.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ays.common.model.entity.BaseEntity;

import java.time.LocalDateTime;

/**
 * Represents user login attempts recorded in the "AYS_USER_LOGIN_ATTEMPT" table.
 *
 * <p>
 * This class extends the BaseEntity class and includes fields for the unique identifier
 * of the login attempt, associated user ID, and the date and time of the last successful login.
 * </p>
 *
 * <p>
 * It provides a method to update the last login time when a login attempt is successful.
 * </p>
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "AYS_USER_LOGIN_ATTEMPT")
public class UserLoginAttemptEntity extends BaseEntity {

    /**
     * The unique identifier for the login attempt record.
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /**
     * The user ID associated with the login attempt.
     */
    @Column(name = "USER_ID")
    private String userId;

    /**
     * The date and time of the last successful login.
     */
    @Column(name = "LAST_LOGIN_AT")
    @Builder.Default
    private LocalDateTime lastLoginAt = LocalDateTime.now();

    public void success() {
        this.lastLoginAt = LocalDateTime.now();
    }

}
