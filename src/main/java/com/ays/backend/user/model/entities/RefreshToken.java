package com.ays.backend.user.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * RefreshToken entity, which holds the information regarding the refresh token.
 */
@Entity
@Table(name = "refreshToken")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Deprecated(since = "Servisi kaldırıldığında silinmeli")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;
}
