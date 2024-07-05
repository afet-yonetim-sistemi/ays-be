package org.ays.auth.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.common.model.BaseDomainModel;
import org.ays.institution.model.Institution;

import java.util.List;

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
        return AysRoleStatus.ACTIVE.equals(this.status);
    }

    /**
     * Checks if the role's status is passive.
     *
     * @return {@code true} if the role's status is {@link AysRoleStatus#PASSIVE}, otherwise {@code false}.
     */
    public boolean isPassive() {
        return AysRoleStatus.PASSIVE.equals(this.status);
    }

    public void activate(){
        this.setStatus(AysRoleStatus.ACTIVE);
    }

    /**
     * Checks if the role's status is deleted.
     *
     * @return {@code true} if the role's status is {@link AysRoleStatus#DELETED}, otherwise {@code false}.
     */
    public boolean isDeleted() {
        return AysRoleStatus.DELETED.equals(this.status);
    }


    /**
     * Sets the role's status to deleted ({@link AysRoleStatus#DELETED}), marking the role as inactive.
     * This method effectively removes the role from active use within the system.
     */
    public void delete() {
        this.status = AysRoleStatus.DELETED;
    }

}
