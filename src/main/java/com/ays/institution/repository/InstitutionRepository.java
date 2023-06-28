package com.ays.institution.repository;

import com.ays.institution.model.entity.InstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * An interface for accessing and managing institutions in a data source with CRUD operations.
 * It extends the JpaRepository interface with InstitutionEntity as the entity type and String as the ID type.
 * The default behavior of the repository can be extended by adding custom methods to this interface.
 */
public interface InstitutionRepository extends JpaRepository<InstitutionEntity, String> {
}
