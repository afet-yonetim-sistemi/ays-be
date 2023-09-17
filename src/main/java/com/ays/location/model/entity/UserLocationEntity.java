package com.ays.location.model.entity;

import com.ays.common.model.entity.BaseEntity;
import com.ays.location.util.AysLocationUtil;
import com.ays.user.model.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
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
