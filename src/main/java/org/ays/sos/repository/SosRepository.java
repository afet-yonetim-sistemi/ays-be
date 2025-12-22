package org.ays.sos.repository;

import org.ays.sos.model.entity.SosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for SOS entity operations.
 */
@Repository
public interface SosRepository extends JpaRepository<SosEntity, String> {

    /**
     * Find all SOS requests by user ID.
     *
     * @param userId the user ID
     * @return list of SOS entities for the user
     */
    List<SosEntity> findByUserId(String userId);

}
