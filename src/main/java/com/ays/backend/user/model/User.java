package com.ays.backend.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {

    private String username;
    private String password;

    private UUID userUUID;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "userId")
    private PhoneNumber phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true)
    private EStatus status;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name = "userId")
    private Set<Type> types = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private Double latitude;
    private Double longitude;

}
