package org.ays.auth.model.request;

import org.ays.auth.model.AdminRegistrationApplicationFilter;
import org.ays.auth.model.enums.AdminRegistrationApplicationStatus;
import org.ays.common.model.AysPageable;
import org.ays.common.model.AysPagingBuilder;
import org.ays.common.model.AysSort;
import org.ays.common.model.TestDataBuilder;

import java.util.List;
import java.util.Set;

public class AdminRegistrationApplicationListRequestBuilder extends TestDataBuilder<AdminRegistrationApplicationListRequest> {

    public AdminRegistrationApplicationListRequestBuilder() {
        super(AdminRegistrationApplicationListRequest.class);
    }

    public AdminRegistrationApplicationListRequestBuilder withValidValues() {
        final List<AysSort.AysOrder> orders = List.of(
                AysSort.AysOrder.builder()
                        .property("createdAt")
                        .direction(AysSort.Direction.DESC)
                        .build()
        );

        return this
                .initializeFilter()
                .withStatuses(Set.of(AdminRegistrationApplicationStatus.WAITING))
                .withPagination(new AysPagingBuilder().withValidValues().build())
                .withOrders(orders);
    }

    private AdminRegistrationApplicationListRequestBuilder initializeFilter() {
        data.setFilter(new AdminRegistrationApplicationFilter());
        return this;
    }

    public AdminRegistrationApplicationListRequestBuilder withFilter(AdminRegistrationApplicationFilter filter) {
        data.setFilter(filter);
        return this;
    }

    public AdminRegistrationApplicationListRequestBuilder withStatuses(Set<AdminRegistrationApplicationStatus> statuses) {
        data.getFilter().setStatuses(statuses);
        return this;
    }

    public AdminRegistrationApplicationListRequestBuilder withPagination(AysPageable aysPageable) {
        data.setPageable(aysPageable);
        return this;
    }

    public AdminRegistrationApplicationListRequestBuilder withOrders(List<AysSort.AysOrder> orders) {
        data.getPageable().setOrders(orders);
        return this;
    }

}
