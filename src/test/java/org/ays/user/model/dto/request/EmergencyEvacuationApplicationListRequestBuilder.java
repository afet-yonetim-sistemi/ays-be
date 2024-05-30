package org.ays.user.model.dto.request;

import org.ays.common.model.AysPaging;
import org.ays.common.model.AysPagingBuilder;
import org.ays.common.model.AysSorting;
import org.ays.common.model.TestDataBuilder;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationListRequest;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationStatus;
import org.springframework.data.domain.Sort;

import java.util.List;

public class EmergencyEvacuationApplicationListRequestBuilder extends TestDataBuilder<EmergencyEvacuationApplicationListRequest> {
    public EmergencyEvacuationApplicationListRequestBuilder() {
        super(EmergencyEvacuationApplicationListRequest.class);
    }

    public EmergencyEvacuationApplicationListRequestBuilder withValidValues() {
        return this
                .withFilter(new FilterBuilder(EmergencyEvacuationApplicationListRequest.Filter.class).withValidValues().build())
                .withPagination(new AysPagingBuilder().withValidValues().build())
                .withSort(AysSorting.of(Sort.by(Sort.Direction.ASC, "createdAt")));
    }

    public EmergencyEvacuationApplicationListRequestBuilder withFilter(EmergencyEvacuationApplicationListRequest.Filter filter) {
        data.setFilter(filter);
        return this;
    }

    public EmergencyEvacuationApplicationListRequestBuilder withPagination(AysPaging aysPaging) {
        data.setPagination(aysPaging);
        return this;
    }

    public EmergencyEvacuationApplicationListRequestBuilder withSort(List<AysSorting> sorting) {
        data.setSort(sorting);
        return this;
    }

    public static class FilterBuilder extends TestDataBuilder<EmergencyEvacuationApplicationListRequest.Filter> {
        public FilterBuilder(Class<EmergencyEvacuationApplicationListRequest.Filter> clazz) {
            super(clazz);
        }

        public FilterBuilder withValidValues() {
            return this;
        }

        public FilterBuilder withReferenceNumber(String referenceNumber) {
            data.setReferenceNumber(referenceNumber);
            return this;
        }

        public FilterBuilder withSeatingCount(Integer seatingCount) {
            data.setSeatingCount(seatingCount);
            return this;
        }

        public FilterBuilder withTargetCity(String targetCity) {
            data.setTargetCity(targetCity);
            return this;
        }

        public FilterBuilder withTargetDistrict(String targetDistrict) {
            data.setTargetDistrict(targetDistrict);
            return this;
        }

        public FilterBuilder withStatuses(List<EmergencyEvacuationApplicationStatus> statuses) {
            data.setStatuses(statuses);
            return this;
        }
    }
}
