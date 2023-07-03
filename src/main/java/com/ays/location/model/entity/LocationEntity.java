package com.ays.location.model.entity;

import com.ays.common.model.entity.BaseEntity;
import com.ays.user.model.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/**
 * Location entity, which holds the information regarding location.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AYS_USER_LOCATION")
public class LocationEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "POINT", columnDefinition = "geometry(Point,4326)")
    private Point point;

    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private UserEntity user;

    public void setPoint(double latitude, double longitude) {
        Coordinate coordinate = new Coordinate(latitude, longitude);
        GeometryFactory geometryFactory = new GeometryFactory();
        this.point = geometryFactory.createPoint(coordinate);
    }

}
