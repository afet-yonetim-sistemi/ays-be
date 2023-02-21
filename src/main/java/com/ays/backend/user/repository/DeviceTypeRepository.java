package com.ays.backend.user.repository;

import com.ays.backend.user.model.entities.DeviceType;
import com.ays.backend.user.model.enums.DeviceNames;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceTypeRepository extends JpaRepository<DeviceType, Integer> {

    Optional<DeviceType> findByName(DeviceNames type);
}
