package org.ays.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.common.model.BaseDomainModel;
import org.ays.user.model.enums.PermissionCategory;

/**
 * Represents a permission entity in the system.
 * <p>
 * The {@link Permission} class extends {@link BaseDomainModel} and includes
 * details such as the permission's unique identifier, name, category, and
 * whether it is a super permission.
 * </p>
 */
@Getter
@Setter
@SuperBuilder
public class Permission extends BaseDomainModel {

    /**
     * The unique identifier for the permission.
     */
    private String id;

    /**
     * The name of the permission.
     */
    private String name;

    /**
     * The category of the permission.
     */
    private PermissionCategory category;

    /**
     * Indicates whether this permission is a super permission.
     */
    private boolean isSuper;

}
