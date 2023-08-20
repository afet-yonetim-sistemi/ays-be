package com.ays.location.model.entity;

import com.ays.common.model.entity.BaseEntity;
import com.ays.location.util.AysLocationUtil;
import com.ays.user.model.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

/**
 * User Location entity, which holds the information regarding location.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
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

    public void setPoint(final Double latitude, final Double longitude) {
        this.point = AysLocationUtil.generatePoint(latitude, longitude);
    }

    public abstract static class UserLocationEntityBuilder<C extends UserLocationEntity, B extends UserLocationEntityBuilder<C, B>> extends BaseEntity.BaseEntityBuilder<C, B> {
        public B point(final Double latitude, final Double longitude) {
            this.point = AysLocationUtil.generatePoint(latitude, longitude);
            return this.self();
        }
    }

}
