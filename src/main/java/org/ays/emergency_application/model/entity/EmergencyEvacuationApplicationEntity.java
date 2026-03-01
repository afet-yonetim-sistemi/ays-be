package org.ays.emergency_application.model.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.common.model.entity.BaseEntity;
import org.ays.emergency_application.model.enums.EmergencyEvacuationApplicationPriority;
import org.ays.emergency_application.model.enums.EmergencyEvacuationApplicationStatus;
import org.ays.institution.model.entity.InstitutionEntity;

import java.util.Optional;

/**
 * A JPA entity class that represents an emergency evacuation entity.
 * The emergency evacuation applications are defined in the AYS_EMERGENCY_EVACUATION_APPLICATION table in the database.
 */
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AYS_EMERGENCY_EVACUATION_APPLICATION")
public class EmergencyEvacuationApplicationEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "INSTITUTION_ID")
    private String institutionId;

    @Column(name = "REFERENCE_NUMBER")
    private String referenceNumber;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "COUNTRY_CODE")
    private String countryCode;

    @Column(name = "LINE_NUMBER")
    private String lineNumber;

    @Column(name = "SOURCE_CITY")
    private String sourceCity;

    @Column(name = "SOURCE_DISTRICT")
    private String sourceDistrict;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "SEATING_COUNT")
    private Integer seatingCount;

    @Column(name = "TARGET_CITY")
    private String targetCity;

    @Column(name = "TARGET_DISTRICT")
    private String targetDistrict;

    @Convert(converter = EmergencyEvacuationApplicationPriorityConverter.class)
    @Column(name = "PRIORITY")
    private EmergencyEvacuationApplicationPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private EmergencyEvacuationApplicationStatus status;

    @Column(name = "APPLICANT_FIRST_NAME")
    private String applicantFirstName;

    @Column(name = "APPLICANT_LAST_NAME")
    private String applicantLastName;

    @Column(name = "APPLICANT_COUNTRY_CODE")
    private String applicantCountryCode;

    @Column(name = "APPLICANT_LINE_NUMBER")
    private String applicantLineNumber;

    @Column(name = "IS_IN_PERSON")
    private Boolean isInPerson;

    @Column(name = "HAS_OBSTACLE_PERSON_EXIST")
    private Boolean hasObstaclePersonExist;

    @Column(name = "NOTES")
    private String notes;


    @OneToOne
    @JoinColumn(name = "INSTITUTION_ID", insertable = false, updatable = false)
    private InstitutionEntity institution;


    /**
     * JPA {@link AttributeConverter} for persisting {@link EmergencyEvacuationApplicationPriority} as an {@link Integer}.
     * <p>
     * Converts the enum to its numeric representation for database storage, and converts the stored numeric value
     * back into the corresponding enum constant when reading.
     * </p>
     */
    @Converter
    public static final class EmergencyEvacuationApplicationPriorityConverter implements AttributeConverter<EmergencyEvacuationApplicationPriority, Integer> {

        /**
         * Converts the given priority into its numeric value for database storage.
         *
         * @param priority the priority to convert (maybe {@code null})
         * @return the numeric value of the priority, or {@code null} if {@code priority} is {@code null}
         */
        @Override
        public Integer convertToDatabaseColumn(EmergencyEvacuationApplicationPriority priority) {
            return Optional.ofNullable(priority)
                    .map(EmergencyEvacuationApplicationPriority::getNumber)
                    .orElse(null);
        }

        /**
         * Converts the stored numeric value into the corresponding priority enum.
         *
         * @param number the numeric value read from the database (may be {@code null})
         * @return the matching {@link EmergencyEvacuationApplicationPriority}, or {@code null} if {@code number} is {@code null}
         * @throws IllegalArgumentException if {@code number} is non-null but does not map to any priority
         */
        @Override
        public EmergencyEvacuationApplicationPriority convertToEntityAttribute(Integer number) {
            return EmergencyEvacuationApplicationPriority.valueOfNumber(number)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown Emergency Evacuation Application Priority Number: " + number));
        }
    }

}
