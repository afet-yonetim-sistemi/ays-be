package org.ays.location.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import org.ays.common.model.entity.BaseEntity;
import org.ays.location.util.AysLocationUtil;
import org.ays.user.model.entity.UserEntity;
import org.locationtech.jts.geom.Point;

/**
 * User Location entity, which holds the information regarding location.
 */
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "AYS_USER_LOCATION")
public class UserLocationEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "POINT", columnDefinition = "ST_GeomFromText(Point, 4326)")
    private Point point;


    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private UserEntity user;

    public void setPoint(final Double longitude, final Double latitude) {
        this.point = AysLocationUtil.generatePoint(longitude, latitude);
    }

    public abstract static class UserLocationEntityBuilder<C extends UserLocationEntity, B extends UserLocationEntityBuilder<C, B>> extends BaseEntity.BaseEntityBuilder<C, B> {
        public B point(final Double longitude, final Double latitude) {
            this.point = AysLocationUtil.generatePoint(longitude, latitude);
            return this.self();
        }
    }

}
