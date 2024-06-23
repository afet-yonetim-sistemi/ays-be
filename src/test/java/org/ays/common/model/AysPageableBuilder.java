package org.ays.common.model;

public class AysPageableBuilder extends TestDataBuilder<AysPageable> {

    public AysPageableBuilder() {
        super(AysPageable.class);
    }

    public AysPageableBuilder withValidValues() {
        return new AysPageableBuilder()
                .withPage(1)
                .withPageSize(1);
    }

    public AysPageableBuilder withPage(int page) {
        data.setPage(page);
        return this;
    }

    public AysPageableBuilder withPageSize(int pageSize) {
        data.setPageSize(pageSize);
        return this;
    }

    public AysPageableBuilder withoutOrders() {
        data.setOrders(null);
        return this;
    }

}
