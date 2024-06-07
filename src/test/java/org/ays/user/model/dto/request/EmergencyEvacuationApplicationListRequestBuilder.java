package org.ays.user.model.dto.request;

import org.ays.common.model.AysPaging;
import org.ays.common.model.AysPagingBuilder;
import org.ays.common.model.AysSorting;
import org.ays.common.model.TestDataBuilder;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationListRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public class EmergencyEvacuationApplicationListRequestBuilder extends TestDataBuilder<EmergencyEvacuationApplicationListRequest> {

    public EmergencyEvacuationApplicationListRequestBuilder() {
        super(EmergencyEvacuationApplicationListRequest.class);
    }

    public EmergencyEvacuationApplicationListRequestBuilder withValidValues() {

        final AysSorting createdAtSort = AysSorting.builder()
                .property("createdAt")
                .direction(Sort.Direction.DESC)
                .build();

        return this
                .withPagination(new AysPagingBuilder().withValidValues().build())
                .withSort(List.of(createdAtSort))
                .initializeFilter();
    }

    public EmergencyEvacuationApplicationListRequestBuilder withPagination(AysPaging aysPaging) {
        data.setPagination(aysPaging);
        return this;
    }

    public EmergencyEvacuationApplicationListRequestBuilder withSort(List<AysSorting> sorting) {
        data.setSort(sorting);
        return this;
    }

    private EmergencyEvacuationApplicationListRequestBuilder initializeFilter() {
        data.setFilter(new EmergencyEvacuationApplicationListRequest.Filter());
        return this;
    }

    public EmergencyEvacuationApplicationListRequestBuilder withFilter(EmergencyEvacuationApplicationListRequest.Filter filter) {
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

}
