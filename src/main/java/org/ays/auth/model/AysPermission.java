package org.ays.auth.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.auth.model.enums.AysPermissionCategory;
import org.ays.common.model.BaseDomainModel;

/**
 * Represents a permission entity in the system.
 * This class extends {@link BaseDomainModel} to inherit common properties such as ID and auditing fields.
 * It encapsulates information about a specific permission granted within the system.
 */
@Getter
@Setter
@SuperBuilder
public class AysPermission extends BaseDomainModel {

    private String id;
    private String name;
    private AysPermissionCategory category;
    private boolean isSuper;

}
