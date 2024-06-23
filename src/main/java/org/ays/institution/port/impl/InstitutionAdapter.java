package org.ays.institution.port.impl;

import lombok.RequiredArgsConstructor;
import org.ays.institution.model.Institution;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.enums.InstitutionStatus;
import org.ays.institution.model.mapper.InstitutionEntityToDomainMapper;
import org.ays.institution.model.mapper.InstitutionToEntityMapper;
import org.ays.institution.port.InstitutionReadPort;
import org.ays.institution.port.InstitutionSavePort;
import org.ays.institution.repository.InstitutionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Adapter class for interacting with institution-related data.
 * <p>
 * This adapter provides methods to read and save institution data, transforming between domain and entity models.
 * It serves as a bridge between the application domain and the data persistence layer, using a repository for database operations.
 * </p>
 * <p>
 * The adapter implements the {@link InstitutionReadPort} and {@link InstitutionSavePort} interfaces,
 * providing methods for finding institutions by status and saving institution data.
 * </p>
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
class InstitutionAdapter implements InstitutionReadPort, InstitutionSavePort {

    private final InstitutionRepository institutionRepository;


    private final InstitutionEntityToDomainMapper institutionEntityToDomainMapper = InstitutionEntityToDomainMapper.initialize();
    private final InstitutionToEntityMapper institutionToEntityMapper = InstitutionToEntityMapper.initialize();


    /**
     * Retrieves a list of institutions by status, ordered by their names in ascending order.
     * <p>
     * This method queries the database for institutions with the specified status and orders them by name in ascending order.
     * The results are mapped from entity to domain objects before being returned.
     * </p>
     *
     * @param status the status of the institutions to retrieve
     * @return a list of institutions with the specified status, ordered by name in ascending order
     */
    @Override
    public List<Institution> findAllByStatusOrderByNameAsc(final InstitutionStatus status) {
        final List<InstitutionEntity> activeInstitutions = institutionRepository.findAllByStatusOrderByNameAsc(status);
        return institutionEntityToDomainMapper.map(activeInstitutions);
    }


    /**
     * Checks if an institution with the given ID exists and has an active status.
     * <p>
     * This method queries the database to determine if an institution with the specified ID exists and is active.
     * </p>
     *
     * @param id the ID of the institution to check
     * @return true if an institution with the specified ID exists and is active, false otherwise
     */
    @Override
    public boolean existsByIdAndIsStatusActive(final String id) {
        return institutionRepository.existsByIdAndStatus(id, InstitutionStatus.ACTIVE);
    }


    /**
     * Saves an institution to the database.
     * <p>
     * This method maps the domain institution object to an entity object and saves it to the database.
     * The saved entity is then mapped back to a domain object and returned.
     * </p>
     *
     * @param institution the institution object to save
     * @return the saved institution object
     */
    @Override
    @Transactional
    public Institution save(final Institution institution) {
        final InstitutionEntity institutionEntity = institutionToEntityMapper.map(institution);
        final InstitutionEntity institutionEntityFromDatabase = institutionRepository.save(institutionEntity);
        return institutionEntityToDomainMapper.map(institutionEntityFromDatabase);
    }

}
