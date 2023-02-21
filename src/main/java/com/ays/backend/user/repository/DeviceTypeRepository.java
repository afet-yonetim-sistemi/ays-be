package com.ays.backend.user.repository;

import java.util.Optional;

import com.ays.backend.user.model.entities.DeviceType;
import com.ays.backend.user.model.enums.DeviceNames;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DeviceTypeRepository performing DB layer operations.
 */
public interface DeviceTypeRepository extends JpaRepository<DeviceType, Integer> {
    Optional<DeviceType> findByName(DeviceNames type);
}
