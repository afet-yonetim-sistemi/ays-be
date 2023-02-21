package com.ays.backend.user.model.entities;

import com.ays.backend.user.model.enums.DeviceNames;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Devicetype entity, holding the name of the devices which can later be assigned to the users.
 */
@Entity
@Table(name = "types")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceType extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true)
    private DeviceNames name;

}
