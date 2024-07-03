package org.ays.auth.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.auth.model.enums.AysPermissionCategory;

/**
 * Represents a permission detail.
 * It encapsulates information detail about a specific permission granted within the system.
 */
@Getter
@Setter
@SuperBuilder
public class AysPermissionDetail {

    private String id;
    private String name;
    private AysPermissionCategory category;

}
