package com.ays.parameter.repository;

import com.ays.parameter.model.entity.AysParameterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * Repository interface for performing CRUD operations on {@link AysParameterEntity} instances.
 * Provides methods for finding {@link AysParameterEntity} instances by name prefix.
 */
public interface AysParameterRepository extends JpaRepository<AysParameterEntity, Long> {

    /**
     * Finds a set of {@link AysParameterEntity} instances whose names start with the specified prefix.
     *
     * @param prefix the prefix to search for
     * @return a set of {@link AysParameterEntity} instances whose names start with the specified prefix
     */
    Set<AysParameterEntity> findByNameStartingWith(String prefix);

}
