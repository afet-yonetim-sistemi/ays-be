package org.ays.auth.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.auth.model.enums.AdminRegistrationApplicationStatus;
import org.ays.common.model.entity.BaseEntity;
import org.ays.institution.model.entity.InstitutionEntity;

/**
 * Entity representing an admin registration application.
 * <p>
 * This class maps to the "AYS_ADMIN_REGISTRATION_APPLICATION" table in the database
 * and holds information about an admin registration application.
 * </p>
 */
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "AYS_ADMIN_REGISTRATION_APPLICATION")
public class AdminRegistrationApplicationEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "REASON")
    private String reason;

    @Column(name = "REJECT_REASON")
    private String rejectReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private AdminRegistrationApplicationStatus status;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private AysUserEntity user;

    @OneToOne
    @JoinColumn(name = "INSTITUTION_ID")
    private InstitutionEntity institution;

}
