package org.ays.auth.model;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;

import java.time.LocalDateTime;

public class AysInvalidTokenBuilder extends TestDataBuilder<AysInvalidToken> {

    public AysInvalidTokenBuilder() {
        super(AysInvalidToken.class);
    }

    public AysInvalidTokenBuilder withValidValues() {
        return new AysInvalidTokenBuilder()
                .withId(1L)
                .withTokenId(AysRandomUtil.generateUUID())
                .withCreatedAt(LocalDateTime.now());
    }

    public AysInvalidTokenBuilder withId(Long id) {
        data.setId(id);
        return this;
    }

    public AysInvalidTokenBuilder withTokenId(String tokenId) {
        data.setTokenId(tokenId);
        return this;
    }

    public AysInvalidTokenBuilder withCreatedAt(LocalDateTime createdAt) {
        data.setCreatedAt(createdAt);
        return this;
    }

}