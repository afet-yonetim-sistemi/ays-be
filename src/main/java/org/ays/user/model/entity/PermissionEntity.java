package org.ays.user.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.entity.BaseEntity;
import org.ays.user.model.enums.PermissionCategory;

/**
 * Represents permissions stored in the "AYS_PERMISSION" table.
 *
 * <p>
 * This class extends the BaseEntity class and includes fields for the unique identifier
 * of the permission, its name, and its category.
 * </p>
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "AYS_PERMISSION")
public class PermissionEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORY")
    private PermissionCategory category;

}
