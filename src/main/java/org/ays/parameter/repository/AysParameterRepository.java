package org.ays.parameter.repository;

import org.ays.parameter.model.entity.AysParameterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on {@link AysParameterEntity} instances.
 */
public interface AysParameterRepository extends JpaRepository<AysParameterEntity, Long> {
}
