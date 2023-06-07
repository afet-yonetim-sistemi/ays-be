package com.ays.institution.model.entity;

import com.ays.common.model.entity.BaseEntity;
import com.ays.institution.model.enums.InstitutionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Institution entity, which holds the information regarding the institutions of the system.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AYS_INSTITUTION")
public class InstitutionEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private InstitutionStatus status;

}
