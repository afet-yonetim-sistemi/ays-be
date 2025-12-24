package org.ays.sos.repository;

import org.ays.sos.model.entity.SosMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for SOS message CRUD operations.
 */
@Repository
public interface SosMessageRepository extends JpaRepository<SosMessageEntity, String> {

    /**
     * Find all messages for a specific SOS request, ordered by creation time.
     *
     * @param sosId the ID of the SOS request
     * @return list of messages ordered by creation time ascending
     */
    List<SosMessageEntity> findBySosIdOrderByCreatedAtAsc(String sosId);

}
