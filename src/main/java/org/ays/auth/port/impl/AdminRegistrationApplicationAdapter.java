package org.ays.auth.port.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AdminRegistrationApplication;
import org.ays.auth.model.AdminRegistrationApplicationFilter;
import org.ays.auth.model.entity.AdminRegistrationApplicationEntity;
import org.ays.auth.model.mapper.AdminRegistrationApplicationEntityToDomainMapper;
import org.ays.auth.model.mapper.AdminRegistrationApplicationToEntityMapper;
import org.ays.auth.port.AdminRegistrationApplicationReadPort;
import org.ays.auth.port.AdminRegistrationApplicationSavePort;
import org.ays.auth.repository.AdminRegistrationApplicationRepository;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter class that implements {@link AdminRegistrationApplicationReadPort} and {@link AdminRegistrationApplicationSavePort}.
 * Retrieves and saves admin registration applications using {@link AdminRegistrationApplicationRepository}.
 */
@Component
@RequiredArgsConstructor
class AdminRegistrationApplicationAdapter implements AdminRegistrationApplicationReadPort, AdminRegistrationApplicationSavePort {

    private final AdminRegistrationApplicationRepository adminRegistrationApplicationRepository;


    private final AdminRegistrationApplicationEntityToDomainMapper adminRegistrationApplicationEntityToDomainMapper = AdminRegistrationApplicationEntityToDomainMapper.initialize();
    private final AdminRegistrationApplicationToEntityMapper adminRegistrationApplicationToEntityMapper = AdminRegistrationApplicationToEntityMapper.initialize();


    /**
     * Retrieves a paginated list of admin registration applications based on provided pagination and filtering criteria.
     *
     * @param aysPageable The pagination configuration.
     * @param filter      The filter criteria to apply when fetching applications.
     * @return A paginated list of {@link AdminRegistrationApplication}.
     */
    @Override
    public AysPage<AdminRegistrationApplication> findAll(final AysPageable aysPageable,
                                                         final AdminRegistrationApplicationFilter filter) {

        final Pageable pageable = aysPageable.toPageable();

        final Specification<AdminRegistrationApplicationEntity> specification = Optional
                .ofNullable(filter)
                .map(AdminRegistrationApplicationFilter::toSpecification)
                .orElse(Specification.allOf());

        final Page<AdminRegistrationApplicationEntity> applicationEntitiesPage = adminRegistrationApplicationRepository
                .findAll(specification, pageable);

        final List<AdminRegistrationApplication> registerApplications = adminRegistrationApplicationEntityToDomainMapper
                .map(applicationEntitiesPage.getContent());

        return AysPage.of(
                filter,
                applicationEntitiesPage,
                registerApplications
        );
    }

    /**
     * Finds an admin registration application by its unique identifier.
     *
     * @param id The unique identifier of the application to find.
     * @return An {@link Optional} containing the found {@link AdminRegistrationApplication}, or empty if not found.
     */
    @Override
    public Optional<AdminRegistrationApplication> findById(final String id) {
        final Optional<AdminRegistrationApplicationEntity> applicationEntity = adminRegistrationApplicationRepository
                .findById(id);
        return applicationEntity.map(adminRegistrationApplicationEntityToDomainMapper::map);
    }

    /**
     * Saves an admin registration application.
     *
     * @param registrationApplication The application to save.
     * @return The saved {@link AdminRegistrationApplication}.
     */
    @Override
    public AdminRegistrationApplication save(final AdminRegistrationApplication registrationApplication) {

        final AdminRegistrationApplicationEntity applicationEntity = adminRegistrationApplicationToEntityMapper
                .map(registrationApplication);

        adminRegistrationApplicationRepository.save(applicationEntity);

        return adminRegistrationApplicationEntityToDomainMapper.map(applicationEntity);
    }

}
