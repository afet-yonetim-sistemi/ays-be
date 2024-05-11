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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.entity.BaseEntity;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.user.model.enums.RoleStatus;

import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "AYS_ROLE")
public class RoleEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "INSTITUTION_ID")
    private String institutionId;

    @Column(name = "NAME")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private RoleStatus status;

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
