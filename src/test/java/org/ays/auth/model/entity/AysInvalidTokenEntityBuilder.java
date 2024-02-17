package org.ays.auth.model.entity;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;

import java.time.LocalDateTime;

public class AysInvalidTokenEntityBuilder extends TestDataBuilder<AysInvalidTokenEntity> {

    public AysInvalidTokenEntityBuilder() {
        super(AysInvalidTokenEntity.class);
    }

    public AysInvalidTokenEntityBuilder withValidValues() {
        return new AysInvalidTokenEntityBuilder()
                .id(1L)
                .tokenId(AysRandomUtil.generateUUID())
                .createdAt(LocalDateTime.now());
    }

    public AysInvalidTokenEntityBuilder id(Long id) {
        data.setId(id);
        return this;
    }

    public AysInvalidTokenEntityBuilder tokenId(String tokenId) {
        data.setTokenId(tokenId);
        return this;
    }

    public AysInvalidTokenEntityBuilder createdAt(LocalDateTime createdAt) {
        data.setCreatedAt(createdAt);
        return this;
    }

}
