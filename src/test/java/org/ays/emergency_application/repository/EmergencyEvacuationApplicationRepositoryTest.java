package org.ays.emergency_application.repository;

import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationEntity;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public interface EmergencyEvacuationApplicationRepositoryTest extends EmergencyEvacuationApplicationRepository {

    Optional<EmergencyEvacuationApplicationEntity> findByFirstName(String firstName);

}
