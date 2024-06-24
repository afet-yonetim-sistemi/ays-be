package org.ays.auth.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.auth.model.enums.RoleStatus;
import org.ays.common.model.BaseDomainModel;
import org.ays.institution.model.Institution;

import java.util.Set;

/**
 * Represents a role entity in the system.
 * This class extends {@link BaseDomainModel} to inherit common properties such as ID and auditing fields.
 * It encapsulates information about a specific role granted within the system.
 */
@Getter
@Setter
@SuperBuilder
public class AysRole extends BaseDomainModel {

    private String id;
    private String name;
    private RoleStatus status;

    private Set<AysPermission> permissions;

    private Institution institution;


    /**
     * Checks if the role's status is active.
     *
     * @return {@code true} if the role's status is {@link RoleStatus#ACTIVE}, otherwise {@code false}.
     */
    public boolean isActive() {
        return RoleStatus.ACTIVE.equals(this.status);
    }

    /**
     * Checks if the role's status is passive.
     *
     * @return {@code true} if the role's status is {@link RoleStatus#PASSIVE}, otherwise {@code false}.
     */
    public boolean isPassive() {
        return RoleStatus.PASSIVE.equals(this.status);
    }

    /**
     * Checks if the role's status is deleted.
     *
     * @return {@code true} if the role's status is {@link RoleStatus#DELETED}, otherwise {@code false}.
     */
    public boolean isDeleted() {
        return RoleStatus.DELETED.equals(this.status);
    }


    /**
     * Sets the role's status to deleted ({@link RoleStatus#DELETED}), marking the role as inactive.
     * This method effectively removes the role from active use within the system.
     */
    public void delete() {
        this.status = RoleStatus.DELETED;
    }

}
