package org.ays.auth.model.request;

import org.ays.auth.model.AysUserFilter;
import org.ays.auth.model.enums.AysUserStatus;
import org.ays.common.model.AysPageable;
import org.ays.common.model.AysPagingBuilder;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.AysSort;
import org.ays.common.model.TestDataBuilder;

import java.util.List;
import java.util.Set;

public class AysUserListRequestBuilder extends TestDataBuilder<AysUserListRequest> {

    public AysUserListRequestBuilder() {
        super(AysUserListRequest.class);
    }

    public AysUserListRequestBuilder withValidValues() {

        final AysSort.AysOrder createdAtSort = AysSort.AysOrder.builder()
                .property("createdAt")
                .direction(AysSort.Direction.DESC)
                .build();

        return this
                .withPagination(new AysPagingBuilder().withValidValues().build())
                .withOrders(List.of(createdAtSort))
                .initializeFilter();
    }

    public AysUserListRequestBuilder withPagination(AysPageable aysPageable) {
        data.setPageable(aysPageable);
        return this;
    }

    public AysUserListRequestBuilder withOrders(List<AysSort.AysOrder> orders) {
        data.getPageable().setOrders(orders);
        return this;
    }

    public AysUserListRequestBuilder withoutOrders() {
        data.getPageable().setOrders(null);
        return this;
    }

    private AysUserListRequestBuilder initializeFilter() {
        data.setFilter(AysUserFilter.builder().build());
        return this;
    }

    public AysUserListRequestBuilder withFirstName(String firstName) {
        data.getFilter().setFirstName(firstName);
        return this;
    }

    public AysUserListRequestBuilder withLastName(String lastName) {
        data.getFilter().setLastName(lastName);
        return this;
    }

    public AysUserListRequestBuilder withCity(String city) {
        data.getFilter().setCity(city);
        return this;
    }

    public AysUserListRequestBuilder withStatuses(Set<AysUserStatus> statuses) {
        data.getFilter().setStatuses(statuses);
        return this;
    }

    public AysUserListRequestBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.getFilter().setPhoneNumber(phoneNumber);
        return this;
    }

}
