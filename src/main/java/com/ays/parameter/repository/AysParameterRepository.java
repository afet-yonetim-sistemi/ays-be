package com.ays.parameter.repository;

import com.ays.parameter.model.entity.AysParameterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AysParameterRepository extends JpaRepository<AysParameterEntity, Long> {

    Set<AysParameterEntity> findByNameStartingWith(String prefix);

}
