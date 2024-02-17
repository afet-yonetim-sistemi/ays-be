package org.ays.common.model;

public class AysPagingBuilder extends TestDataBuilder<AysPaging> {

    public AysPagingBuilder() {
        super(AysPaging.class);
    }

    public AysPagingBuilder withValidValues() {
        return this
                .withPage(1)
                .withPageSize(10);
    }

    public AysPagingBuilder withPage(int page) {
        data.setPage(page);
        return this;
    }

    public AysPagingBuilder withPageSize(int pageSize) {
        data.setPageSize(pageSize);
        return this;
    }

}
