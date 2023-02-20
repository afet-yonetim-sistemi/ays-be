package com.ays.backend.user.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@RequiredArgsConstructor
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(length = 30, unique = true , nullable = false)
    private UserRole name;
    public Role(UserRole name){
        this.name = name;
    }

}
