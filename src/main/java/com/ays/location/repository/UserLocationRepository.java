package com.ays.location.repository;

import com.ays.location.model.entity.UserLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on LocationEntity objects.
 */
public interface UserLocationRepository extends JpaRepository<UserLocationEntity, Long> {

}
