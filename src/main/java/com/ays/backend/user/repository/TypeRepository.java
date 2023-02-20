package com.ays.backend.user.repository;

import com.ays.backend.user.model.DeviceType;
import com.ays.backend.user.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeRepository extends JpaRepository<Type, Integer> {

    Optional<Type> findByName(DeviceType type);
}
