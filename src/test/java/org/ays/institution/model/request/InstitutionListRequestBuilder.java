package org.ays.institution.model.request;

import org.ays.common.model.AysPageable;
import org.ays.common.model.AysPageableBuilder;
import org.ays.common.model.AysSort;
import org.ays.common.model.TestDataBuilder;
import org.ays.institution.model.InstitutionFilter;
import org.ays.institution.model.enums.InstitutionStatus;

import java.util.List;
import java.util.Set;

public class InstitutionListRequestBuilder extends TestDataBuilder<InstitutionListRequest> {

    public InstitutionListRequestBuilder() {
        super(InstitutionListRequest.class);
    }

    public InstitutionListRequestBuilder withValidValues() {

        final AysSort.AysOrder createdAtSort = AysSort.AysOrder.builder()
                .property("createdAt")
                .direction(AysSort.Direction.DESC)
                .build();

        return this
                .withPageable(new AysPageableBuilder().withValidValues().build())
                .withOrders(List.of(createdAtSort))
                .initializeFilter();
    }

    public InstitutionListRequestBuilder withPageable(AysPageable aysPageable) {
        data.setPageable(aysPageable);
        return this;
    }

    public InstitutionListRequestBuilder withOrders(List<AysSort.AysOrder> orders) {
        data.getPageable().setOrders(orders);
        return this;
    }

    public InstitutionListRequestBuilder withoutOrders() {
        data.getPageable().setOrders(null);
        return this;
    }

    private InstitutionListRequestBuilder initializeFilter() {
        data.setFilter(InstitutionFilter.builder().build());
        return this;
    }

    public InstitutionListRequestBuilder withName(String name) {
        data.getFilter().setName(name);
        return this;
    }

    public InstitutionListRequestBuilder withStatuses(Set<InstitutionStatus> statuses) {
        data.getFilter().setStatuses(statuses);
        return this;
    }

}
