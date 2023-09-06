package com.ays.location.model.entity;

import com.ays.common.model.TestDataBuilder;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;

public class UserLocationEntityBuilder extends TestDataBuilder<UserLocationEntity> {

    public UserLocationEntityBuilder() {
        super(UserLocationEntity.class);
    }

    public UserLocationEntityBuilder withValidFields() {

        UserEntity userEntity = new UserEntityBuilder()
                .withValidFields()
                .build();

        return this
                .withId(1L)
                .withUserId(userEntity.getId())
                .withUser(userEntity)
                .withPoint(1.0, 2.0);
    }

    public UserLocationEntityBuilder withId(Long id) {
        data.setId(id);
        return this;
    }

    public UserLocationEntityBuilder withUserId(String userId) {
        data.setUserId(userId);
        return this;
    }

    public UserLocationEntityBuilder withUser(UserEntity userEntity) {
        data.setUser(userEntity);
        return this;
    }

    public UserLocationEntityBuilder withPoint(Double longitude, Double latitude) {
        data.setPoint(longitude, latitude);
        return this;
    }
}
