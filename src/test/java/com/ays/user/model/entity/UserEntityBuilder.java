package com.ays.user.model.entity;

import com.ays.common.model.TestDataBuilder;
import com.ays.user.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;

public class UserEntityBuilder extends TestDataBuilder<UserEntity> {

    public UserEntityBuilder() {
        super(UserEntity.class);
    }

    public static final Page<UserEntity> VALID_PAGE_OF_USER_ENTITIES = new PageImpl<>(
            Collections.singletonList(new UserEntityBuilder().build())
    );

    public UserEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public UserEntityBuilder withStatus(UserStatus status) {
        data.setStatus(status);
        return this;
    }

}
