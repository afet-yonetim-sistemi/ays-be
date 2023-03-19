package com.ays.backend.user.model.entities;

import com.ays.backend.user.controller.payload.request.AdminRegisterRequest;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.model.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

/**
 * Users entity, which holds the information regarding the system user.
 */
@Entity
@Table(name = "user",
        uniqueConstraints={
        @UniqueConstraint(name = "UniqueMobileNumber", columnNames = {"countryCode", "lineNumber"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Organization organization;

    @Enumerated(EnumType.ORDINAL)
    @JoinColumn(name = "type_id")
    private UserRole userRole;

    @Enumerated(EnumType.ORDINAL)
    @JoinColumn(name = "status_id")
    @Column(length = 20, nullable = false)
    private UserStatus status;

    @Column(nullable = false)
    private Integer countryCode;

    @Column(nullable = false)
    private Integer lineNumber;

    @Column
    private LocalDateTime lastLoginDate;

    @Column(name = "organization_id")
    private Long organizationId;

    public static User from(AdminRegisterRequest registerRequest, PasswordEncoder passwordEncoder) {

        return User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .userRole(UserRole.ROLE_ADMIN)
                .countryCode(registerRequest.getCountryCode())
                .lineNumber(registerRequest.getLineNumber())
                .email(registerRequest.getEmail())
                .lastLoginDate(LocalDateTime.now())
                .organizationId(registerRequest.getOrganizationId())
                .status(UserStatus.getById(registerRequest.getStatusValue()))
                .build();

    }

}
