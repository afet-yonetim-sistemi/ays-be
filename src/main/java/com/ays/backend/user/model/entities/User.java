package com.ays.backend.user.model.entities;

import java.time.LocalDateTime;

import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.model.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Users entity, which holds the information regarding the system user.
 */
@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne
    @Column(nullable = false)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Enumerated(EnumType.ORDINAL)
    @JoinColumn(name = "type_id")
    private UserRole userRole;

    @Enumerated(EnumType.ORDINAL)
    @JoinColumn(name = "status_id")
    @Column(length = 20, unique = true, nullable = false)
    private UserStatus status;

    @Column(nullable = false)
    private int countryCode;

    @Column(nullable = false)
    private int lineNumber;

    @Column
    private LocalDateTime lastLoginDate;

}
