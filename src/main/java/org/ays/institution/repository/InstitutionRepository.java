package org.ays.institution.repository;

import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.enums.InstitutionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * An interface for accessing and managing institutions in a data source with CRUD operations.
 * It extends the JpaRepository interface with InstitutionEntity as the entity type and String as the ID type.
 * The default behavior of the repository can be extended by adding custom methods to this interface.
 */
public interface InstitutionRepository extends JpaRepository<InstitutionEntity, String> {

    /**
     * Find all institutions by status
     *
     * @param status the status of the institutions to retrieve
     * @return the list of institutions
     */
    List<InstitutionEntity> findAllByStatusOrderByNameAsc(InstitutionStatus status);


    /**
     * Checks if an institution exists by id and status.
     *
     * @param id     the id of the institution
     * @param status the status of the institution
     * @return true if the institution exists, false otherwise
     */
    boolean existsByIdAndStatus(String id, InstitutionStatus status);


    /**
     * Checks if an institution exists by id and active status.
     *
     * @param id the id of the institution
     * @return true if the institution exists, false otherwise
     */
    default boolean existsActiveById(String id) {
        return this.existsByIdAndStatus(id, InstitutionStatus.ACTIVE);
    }
}
