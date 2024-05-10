package org.ays.user.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.entity.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "AYS_USER_LOGIN_ATTEMPT")
public class UserLoginAttemptEntity extends BaseEntity {

    /**
     * The unique identifier for the login attempt record.
     */
    @Id
    @Column(name = "ID")
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
    private LocalDateTime lastLoginAt;

    public void success() {
        this.lastLoginAt = LocalDateTime.now();
    }

}
