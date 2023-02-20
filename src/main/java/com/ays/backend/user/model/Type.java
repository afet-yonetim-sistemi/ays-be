package com.ays.backend.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "types")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Type extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true)
    private DeviceType name;

}
