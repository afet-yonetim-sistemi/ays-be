package com.ays.backend.user.model.entities;

import com.ays.backend.user.controller.payload.request.AdminRegisterRequest;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.model.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

/**
 * Users entity, which holds the information regarding the system user.
 */
@Entity
@Table(name = "user",
        uniqueConstraints = {
                @UniqueConstraint(name = "UniqueMobileNumber", columnNames = {"countryCode", "lineNumber"})
        })
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {

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
    private OrganizationEntity organization;

    @Enumerated(EnumType.ORDINAL)
    @JoinColumn(name = "type_id")
    private UserRole role;

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

    public void deleteUser() {
        this.status = UserStatus.DELETED;
    }

    public static UserEntity from(AdminRegisterRequest registerRequest, PasswordEncoder passwordEncoder) {

        return UserEntity.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .role(UserRole.ROLE_ADMIN)
                .countryCode(registerRequest.getCountryCode())
                .lineNumber(registerRequest.getLineNumber())
                .email(registerRequest.getEmail())
                .lastLoginDate(LocalDateTime.now())
                //.organizationId(registerRequest.getOrganizationId())
                .status(UserStatus.WAITING)
                .build();

    }

}
