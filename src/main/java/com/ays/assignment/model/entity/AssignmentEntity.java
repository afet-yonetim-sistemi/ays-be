package com.ays.assignment.model.entity;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.model.entity.BaseEntity;
import com.ays.institution.model.entity.InstitutionEntity;
import com.ays.location.util.AysLocationUtil;
import com.ays.user.model.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

/**
 * Assignment entity, which holds the information regarding assignment.
 */
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "AYS_ASSIGNMENT")
public class AssignmentEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "INSTITUTION_ID")
    private String institutionId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "COUNTRY_CODE")
    private String countryCode;

    @Column(name = "LINE_NUMBER")
    private String lineNumber;

    @Column(name = "POINT", columnDefinition = "ST_GeomFromText(Point, 4326)")
    private Point point;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private AssignmentStatus status;

    @OneToOne
    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private InstitutionEntity institution;

    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private UserEntity user;

    public boolean isAvailable() {
        return AssignmentStatus.AVAILABLE.equals(this.status);
    }

    public boolean isAssigned() {
        return AssignmentStatus.ASSIGNED.equals(this.status);
    }

    public void available() {
        this.status = AssignmentStatus.AVAILABLE;
        this.userId = null;
    }

    public void assign() {
        this.status = AssignmentStatus.ASSIGNED;
    }

    public void start() {
        this.status = AssignmentStatus.IN_PROGRESS;
    }

    public void done() {
        this.status = AssignmentStatus.DONE;
    }

    public void reserve(String userId, String institutionId) {
        this.status = AssignmentStatus.RESERVED;
        this.userId = userId;
        this.institutionId = institutionId;
    }

    public void update(AssignmentEntity assignmentEntity) {
        this.description = assignmentEntity.getDescription();
        this.firstName = assignmentEntity.getFirstName();
        this.lastName = assignmentEntity.getLastName();
        this.countryCode = assignmentEntity.getCountryCode();
        this.lineNumber = assignmentEntity.getLineNumber();
        this.point = assignmentEntity.getPoint();
    }

    public abstract static class AssignmentEntityBuilder<C extends AssignmentEntity, B extends AssignmentEntityBuilder<C, B>> extends BaseEntity.BaseEntityBuilder<C, B> {
        public B point(final Double longitude, final Double latitude) {
            this.point = AysLocationUtil.generatePoint(longitude, latitude);
            return this.self();
        }
    }
}
