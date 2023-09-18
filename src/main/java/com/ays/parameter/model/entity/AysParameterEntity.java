package com.ays.parameter.model.entity;

import com.ays.common.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * A JPA entity class that represents a parameter in the system.
 * The parameters are defined in the AYS_PARAMETER table in the database.
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "AYS_PARAMETER")
public class AysParameterEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DEFINITION")
    private String definition;

}
