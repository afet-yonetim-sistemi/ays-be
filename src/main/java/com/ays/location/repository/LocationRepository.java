package com.ays.location.repository;

import com.ays.location.model.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on LocationEntity objects.
 */
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

}
