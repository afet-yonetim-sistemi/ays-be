package com.ays.organization.model.entity;

import com.ays.common.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Organization entity, which holds the information regarding the organizations of the system.
 */
@Entity
@Table(name = "AYS_ORGANIZATION")
public class OrganizationEntity extends BaseEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;
}
