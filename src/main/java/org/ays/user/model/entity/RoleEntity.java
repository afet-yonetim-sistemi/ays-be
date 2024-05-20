package org.ays.user.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ays.common.model.entity.BaseEntity;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.user.model.enums.RoleStatus;

import java.util.Set;

/**
 * Represents roles stored in the "AYS_ROLE" table.
 *
 * <p>
 * This class extends the BaseEntity class and includes fields for the unique identifier
 * of the role, institution ID, name, and status.
 * </p>
 *
 * <p>
 * It provides methods to check whether a role is active, passive, or deleted, and to delete
 * a role by updating its status.
 * </p>
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "AYS_ROLE")
public class RoleEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    @Builder.Default
    private String id = AysRandomUtil.generateUUID();

    @Column(name = "INSTITUTION_ID")
    private String institutionId;

    @Column(name = "NAME")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    @Builder.Default
    private RoleStatus status = RoleStatus.ACTIVE;

    @OneToOne
    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private InstitutionEntity institution;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "AYS_ROLE_PERMISSION_RELATION",
            joinColumns = @JoinColumn(name = "ROLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID")
    )
    private Set<PermissionEntity> permissions;


    public boolean isActive() {
        return RoleStatus.ACTIVE.equals(this.status);
    }

    public boolean isPassive() {
        return RoleStatus.PASSIVE.equals(this.status);
    }

    public boolean isDeleted() {
        return RoleStatus.DELETED.equals(this.status);
    }

    public void delete() {
        this.status = RoleStatus.DELETED;
    }

}
