package com.ays.auth.model.entity;

import com.ays.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * AysInvalidTokenEntity is an entity class that represents an invalid token.
 * It extends the BaseEntity class and maps to the "AYS_INVALID_TOKEN" table in the database.
 */
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "AYS_INVALID_TOKEN")
public class AysInvalidTokenEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TOKEN_ID")
    private String tokenId;

}
