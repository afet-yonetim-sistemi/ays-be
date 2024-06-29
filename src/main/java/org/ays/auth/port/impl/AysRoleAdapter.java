package org.ays.auth.port.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.entity.AysRoleEntity;
import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.auth.model.mapper.AysRoleEntityToDomainMapper;
import org.ays.auth.model.mapper.AysRoleToEntityMapper;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysRoleSavePort;
import org.ays.auth.repository.AysRoleRepository;
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
        Optional<AysRoleEntity> roleEntity = roleRepository.findByName(name);
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
        final AysRoleEntity roleEntityFromDatabase = roleRepository.save(roleEntity);
        return roleEntityToDomainMapper.map(roleEntityFromDatabase);
    }

}
