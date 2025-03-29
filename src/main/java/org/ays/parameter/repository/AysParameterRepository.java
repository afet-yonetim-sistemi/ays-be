package org.ays.parameter.repository;

import org.ays.parameter.model.AysParameter;
import org.ays.parameter.model.entity.AysParameterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link AysParameterEntity} instances.
 * Provides methods for finding {@link AysParameterEntity} instances by name prefix.
 */
public interface AysParameterRepository extends JpaRepository<AysParameterEntity, Long> {

    /**
     * Finds an {@link AysParameterEntity} instances by name.
     *
     * @param name the name to search for
     * @return an {@link Optional} containing the {@link AysParameter} entity if found, otherwise empty
     */
    Optional<AysParameterEntity> findByName(String name);

}
