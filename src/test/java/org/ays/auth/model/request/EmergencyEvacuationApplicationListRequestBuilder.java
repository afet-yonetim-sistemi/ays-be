package org.ays.auth.model.request;

import org.ays.common.model.AysPageable;
import org.ays.common.model.AysPagingBuilder;
import org.ays.common.model.AysSort;
import org.ays.common.model.TestDataBuilder;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationStatus;
import org.ays.emergency_application.model.filter.EmergencyEvacuationApplicationFilter;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationListRequest;

import java.util.List;
import java.util.Set;

public class EmergencyEvacuationApplicationListRequestBuilder extends TestDataBuilder<EmergencyEvacuationApplicationListRequest> {

    public EmergencyEvacuationApplicationListRequestBuilder() {
        super(EmergencyEvacuationApplicationListRequest.class);
    }

    public EmergencyEvacuationApplicationListRequestBuilder withValidValues() {

        final AysSort.AysOrder createdAtSort = AysSort.AysOrder.builder()
                .property("createdAt")
                .direction(AysSort.Direction.DESC)
                .build();

        return this
                .withPagination(new AysPagingBuilder().withValidValues().build())
                .withOrders(List.of(createdAtSort))
                .initializeFilter();
    }

    public EmergencyEvacuationApplicationListRequestBuilder withPagination(AysPageable aysPageable) {
        data.setPageable(aysPageable);
        return this;
    }

    public EmergencyEvacuationApplicationListRequestBuilder withOrders(List<AysSort.AysOrder> orders) {
        data.getPageable().setOrders(orders);
        return this;
    }

    public EmergencyEvacuationApplicationListRequestBuilder withoutOrders() {
        data.getPageable().setOrders(null);
        return this;
    }

    private EmergencyEvacuationApplicationListRequestBuilder initializeFilter() {
        data.setFilter(new EmergencyEvacuationApplicationFilter());
        return this;
    }

    public EmergencyEvacuationApplicationListRequestBuilder withFilter(EmergencyEvacuationApplicationFilter filter) {
        data.setFilter(filter);
        return this;
    }

    public EmergencyEvacuationApplicationListRequestBuilder withReferenceNumber(String referenceNumber) {
        data.getFilter().setReferenceNumber(referenceNumber);
        return this;
    }

    public EmergencyEvacuationApplicationListRequestBuilder withSourceCity(String sourceCity) {
        data.getFilter().setSourceCity(sourceCity);
        return this;
    }

    public EmergencyEvacuationApplicationListRequestBuilder withSourceDistrict(String sourceDistrict) {
        data.getFilter().setSourceDistrict(sourceDistrict);
        return this;
    }

    public EmergencyEvacuationApplicationListRequestBuilder withSeatingCount(Integer seatingCount) {
        data.getFilter().setSeatingCount(seatingCount);
        return this;
    }

    public EmergencyEvacuationApplicationListRequestBuilder withTargetCity(String targetCity) {
        data.getFilter().setTargetCity(targetCity);
        return this;
    }

    public EmergencyEvacuationApplicationListRequestBuilder withTargetDistrict(String targetDistrict) {
        data.getFilter().setTargetDistrict(targetDistrict);
        return this;
    }

    public EmergencyEvacuationApplicationListRequestBuilder withStatuses(Set<EmergencyEvacuationApplicationStatus> statuses) {
        data.getFilter().setStatuses(statuses);
        return this;
    }

}
