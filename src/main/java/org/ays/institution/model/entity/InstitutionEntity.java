package org.ays.institution.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.entity.BaseEntity;
import org.ays.institution.model.enums.InstitutionStatus;

/**
 * Institution entity, which holds the information regarding the institutions of the system.
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "AYS_INSTITUTION")
public class InstitutionEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "FE_URL")
    private String feUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private InstitutionStatus status;

}
