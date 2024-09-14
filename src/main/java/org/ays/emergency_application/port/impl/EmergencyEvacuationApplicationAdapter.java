package org.ays.emergency_application.port.impl;

import lombok.RequiredArgsConstructor;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageable;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationEntity;
import org.ays.emergency_application.model.filter.EmergencyEvacuationApplicationFilter;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationEntityToDomainMapper;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationToEntityMapper;
import org.ays.emergency_application.port.EmergencyEvacuationApplicationReadPort;
import org.ays.emergency_application.port.EmergencyEvacuationApplicationSavePort;
import org.ays.emergency_application.repository.EmergencyEvacuationApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter for handling operations related to emergency evacuation applications.
 * <p>
 * This class implements the {@link EmergencyEvacuationApplicationReadPort} and {@link EmergencyEvacuationApplicationSavePort}
 * interfaces to provide functionality for reading and saving emergency evacuation application data.
 * It acts as a bridge between the domain model and the data access layer, using mappers for converting between entities and domain objects.
 * </p>
 */
@Component
@RequiredArgsConstructor
class EmergencyEvacuationApplicationAdapter implements EmergencyEvacuationApplicationReadPort, EmergencyEvacuationApplicationSavePort {

    private final EmergencyEvacuationApplicationRepository emergencyEvacuationApplicationRepository;


    private final EmergencyEvacuationApplicationEntityToDomainMapper emergencyEvacuationApplicationEntityToDomainMapper = EmergencyEvacuationApplicationEntityToDomainMapper.initialize();
    private final EmergencyEvacuationApplicationToEntityMapper emergencyEvacuationApplicationToEntityMapper = EmergencyEvacuationApplicationToEntityMapper.initialize();


    /**
     * Retrieves a paginated list of emergency evacuation applications based on the provided pageable and filter.
     * <p>
     * Converts the {@link AysPageable} and {@link EmergencyEvacuationApplicationFilter} into a JPA {@link Pageable}
     * and {@link Specification}, respectively, to query the repository.
     * Maps the results to domain objects using the {@link EmergencyEvacuationApplicationEntityToDomainMapper}.
     * </p>
     *
     * @param aysPageable the pageable information
     * @param filter      the filtering criteria
     * @return a page of {@link EmergencyEvacuationApplication} objects
     */
    @Override
    public AysPage<EmergencyEvacuationApplication> findAll(final AysPageable aysPageable,
                                                           final EmergencyEvacuationApplicationFilter filter) {

        final Pageable pageable = aysPageable.toPageable();

        final Specification<EmergencyEvacuationApplicationEntity> specification = Optional
                .ofNullable(filter)
                .map(EmergencyEvacuationApplicationFilter::toSpecification)
                .orElse(Specification.allOf());

        final Page<EmergencyEvacuationApplicationEntity> emergencyEvacuationApplicationEntities = emergencyEvacuationApplicationRepository
                .findAll(specification, pageable);

        final List<EmergencyEvacuationApplication> emergencyEvacuationApplications = emergencyEvacuationApplicationEntityToDomainMapper
                .map(emergencyEvacuationApplicationEntities.getContent());

        return AysPage.of(
                filter,
                emergencyEvacuationApplicationEntities,
                emergencyEvacuationApplications
        );
    }


    /**
     * Retrieves an emergency evacuation application by its ID.
     * <p>
     * Fetches the entity from the repository and converts it to a domain object using the {@link EmergencyEvacuationApplicationEntityToDomainMapper}.
     * </p>
     *
     * @param id the ID of the emergency evacuation application
     * @return an {@link Optional} containing the {@link EmergencyEvacuationApplication} if found, or empty if not found
     */
    @Override
    public Optional<EmergencyEvacuationApplication> findById(final String id) {
        final Optional<EmergencyEvacuationApplicationEntity> applicationEntity = emergencyEvacuationApplicationRepository
                .findById(id);
        return applicationEntity.map(emergencyEvacuationApplicationEntityToDomainMapper::map);
    }


    /**
     * Saves a new or existing emergency evacuation application.
     * <p>
     * Converts the domain object to an entity using the {@link EmergencyEvacuationApplicationToEntityMapper},
     * saves it to the repository, and maps the persisted entity back to a domain object.
     * This method is transactional.
     * </p>
     *
     * @param emergencyEvacuationApplication the emergency evacuation application to save
     * @return the saved {@link EmergencyEvacuationApplication}
     */
    @Override
    public EmergencyEvacuationApplication save(final EmergencyEvacuationApplication emergencyEvacuationApplication) {
        final EmergencyEvacuationApplicationEntity applicationEntity = emergencyEvacuationApplicationToEntityMapper
                .map(emergencyEvacuationApplication);
        emergencyEvacuationApplicationRepository.save(applicationEntity);
        return emergencyEvacuationApplicationEntityToDomainMapper.map(applicationEntity);
    }

}
