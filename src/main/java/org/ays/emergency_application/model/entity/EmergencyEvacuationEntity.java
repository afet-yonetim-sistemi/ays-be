package org.ays.emergency_application.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ays.common.model.entity.BaseEntity;
import org.ays.institution.model.entity.InstitutionEntity;

/**
 * A JPA entity class that represents an emergency evacuation entity.
 * The emergency evacuation applications are defined in the AYS_EMERGENCY_EVACUATION_APPLICATION table in the database.
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AYS_EMERGENCY_EVACUATION_APPLICATION")
public class EmergencyEvacuationEntity extends BaseEntity {
    @Id
    @Column(name = "ID")
    private String id;

    @OneToOne
    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private InstitutionEntity institutionEntity;

    @Column(name = "REFERENCE_NUMBER")
    private Long referenceNumber;

    @Column(name = "FIRST_NAME")
    @Size(max = 255)
    private String firstName;

    @Column(name = "LAST_NAME")
    @Size(max = 255)
    private String lastName;

    @Column(name = "COUNTRY_CODE")
    @Size(max = 7)
    private String countryCode;

    @Column(name = "LINE_NUMBER")
    @Size(max = 13)
    private String lineNumber;

    @Column(name = "ADDRESS")
    @Size(max = 250)
    private String address;

    @Column(name = "PERSON_COUNT", length = 3)
    private int personCount;

    @Column(name = "HAS_OBSTACLE_PERSON_EXIST")
    private boolean hasObstaclePersonExist;

    @Column(name = "IS_IN_PERSON")
    private boolean isInPerson;

    @Column(name = "TARGET_CITY")
    @Size(max = 100)
    private String targetCity;

    @Column(name = "TARGET_DISTRICT")
    @Size(max = 50)
    private String targetDistrict;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private EmergencyEvacuationApplicationStatus status;

    @Column(name = "APPLICANT_FIRST_NAME")
    @Size(max = 255)
    private String applicantFirstName;

    @Column(name = "APPLICANT_LAST_NAME")
    @Size(max = 255)
    private String applicantLastName;

    @Column(name = "APPLICANT_COUNTRY_CODE")
    @Size(max = 7)
    private String applicantCountryCode;

    @Column(name = "APPLICANT_LINE_NUMBER")
    @Size(max = 13)
    private String applicantLineNumber;

}
