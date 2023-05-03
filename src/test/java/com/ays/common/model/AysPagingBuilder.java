package com.ays.common.model;

public class AysPagingBuilder extends TestDataBuilder<AysPaging> {

    public AysPagingBuilder() {
        super(AysPaging.class);
    }

    public static final AysPagingBuilder VALID = new AysPagingBuilder()
            .withPage(1L)
            .withPageSize(10L);

    public AysPagingBuilder withPage(Long page) {
        data.setPage(page);
        return this;
    }

    public AysPagingBuilder withPageSize(Long pageSize) {
        data.setPageSize(pageSize);
        return this;
    }

}
