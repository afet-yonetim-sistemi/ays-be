package org.ays.auth.port.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysRoleFilter;
import org.ays.auth.model.entity.AysRoleEntity;
import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.auth.model.mapper.AysRoleEntityToDomainMapper;
import org.ays.auth.model.mapper.AysRoleToEntityMapper;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysRoleSavePort;
import org.ays.auth.repository.AysRoleRepository;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Adapter class implementing both {@link AysRoleReadPort} and {@link AysRoleSavePort} interfaces.
 * Retrieves {@link AysRole} entities from the repository, saves {@link AysRole} entities to the database,
 * and maps between domain models and entity models.
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
class AysRoleAdapter implements AysRoleReadPort, AysRoleSavePort {

    private final AysRoleRepository roleRepository;


    private final AysRoleToEntityMapper roleToEntityMapper = AysRoleToEntityMapper.initialize();
    private final AysRoleEntityToDomainMapper roleEntityToDomainMapper = AysRoleEntityToDomainMapper.initialize();


    /**
     * Finds all roles with pagination and optional filtering.
     * <p>
     * This method uses the provided {@link AysPageable} for pagination and {@link AysRoleFilter} for filtering.
     * It returns a paginated list of {@link AysRole} domain models.
     * </p>
     *
     * @param aysPageable the pagination configuration
     * @param filter      the filter for roles
     * @return a paginated list of roles
     */
    @Override
    public AysPage<AysRole> findAll(final AysPageable aysPageable, final AysRoleFilter filter) {

        final Pageable pageable = aysPageable.toPageable();
        final Specification<AysRoleEntity> specification = filter.toSpecification();

        final Page<AysRoleEntity> roleEntitiesPage = roleRepository.findAll(specification, pageable);

        final List<AysRole> roles = roleEntityToDomainMapper.map(roleEntitiesPage.getContent());

        return AysPage.of(filter, roleEntitiesPage, roles);
    }


    /**
     * Retrieves all active {@link AysRole} entities associated with a specific institution ID.
     *
     * @param institutionId The ID of the institution to filter roles by.
     * @return A set of active {@link AysRole} entities found for the institution.
     */
    @Override
    public List<AysRole> findAllActivesByInstitutionId(final String institutionId) {
        List<AysRoleEntity> roleEntities = roleRepository.findAllByInstitutionIdAndStatus(institutionId, AysRoleStatus.ACTIVE);
        return roleEntityToDomainMapper.map(roleEntities);
    }


    /**
     * Retrieves an {@link AysRole} by its name.
     *
     * @param name The name of the role to retrieve.
     * @return An optional containing the {@link AysRole} if found, otherwise empty.
     */
    @Override
    public Optional<AysRole> findByName(final String name) {
        final Optional<AysRoleEntity> roleEntity = roleRepository.findByName(name);
        return roleEntity.map(roleEntityToDomainMapper::map);
    }


    /**
     * Saves an {@link AysRole} to the database.
     *
     * @param role The {@link AysRole} to save.
     * @return The saved {@link AysRole} after persistence.
     */
    @Override
    @Transactional
    public AysRole save(final AysRole role) {
        final AysRoleEntity roleEntity = roleToEntityMapper.map(role);
        roleRepository.save(roleEntity);
        return roleEntityToDomainMapper.map(roleEntity);
    }

}
