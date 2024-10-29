package org.ays.auth.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.common.model.BaseDomainModel;
import org.ays.institution.model.Institution;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a role entity in the system.
 * This class extends {@link BaseDomainModel} to inherit common properties such as ID and auditing fields.
 * It encapsulates information about a specific role granted within the system.
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AysRole extends BaseDomainModel {

    private String id;
    private String name;
    private AysRoleStatus status;

    private List<AysPermission> permissions;

    private Institution institution;


    /**
     * Checks if the role's status is active.
     *
     * @return {@code true} if the role's status is {@link AysRoleStatus#ACTIVE}, otherwise {@code false}.
     */
    public boolean isActive() {
        return this.status == AysRoleStatus.ACTIVE;
    }

    /**
     * Checks if the role's status is passive.
     *
     * @return {@code true} if the role's status is {@link AysRoleStatus#PASSIVE}, otherwise {@code false}.
     */
    public boolean isPassive() {
        return this.status == AysRoleStatus.PASSIVE;
    }

    /**
     * Checks if the role's status is deleted.
     *
     * @return {@code true} if the role's status is {@link AysRoleStatus#DELETED}, otherwise {@code false}.
     */
    public boolean isDeleted() {
        return this.status == AysRoleStatus.DELETED;
    }


    /**
     * Updates the role with the provided information.
     * This method updates the role's name and permissions based on the provided values.
     *
     * @param name          The new name of the role.
     * @param permissionIds The IDs of the permissions to be assigned to the role.
     * @param updatedUser   The user who is updating the role.
     */
    public void update(final String name,
                       final Set<String> permissionIds,
                       final String updatedUser) {

        this.name = name;
        this.permissions = permissionIds.stream()
                .map(roleId -> AysPermission.builder().id(roleId).build())
                .collect(Collectors.toList());
        this.updatedUser = updatedUser;
    }

    /**
     * Activates the role by setting its status to {@link AysRoleStatus#ACTIVE}.
     * This method should be called when the role needs to be marked as active in the system.
     */
    public void activate() {
        this.setStatus(AysRoleStatus.ACTIVE);
    }

    /**
     * Passivates the role by setting its status to {@link AysRoleStatus#PASSIVE}.
     * This method should be called when the role needs to be marked passive in the system.
     */
    public void passivate() {
        this.setStatus(AysRoleStatus.PASSIVE);
    }

    /**
     * Sets the role's status to deleted ({@link AysRoleStatus#DELETED}), marking the role as inactive.
     * This method effectively removes the role from active use within the system.
     */
    public void delete() {
        this.status = AysRoleStatus.DELETED;
    }

}
