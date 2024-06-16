package org.ays.user.model.dto.request;

import org.ays.common.model.AysPaging;
import org.ays.common.model.AysPagingBuilder;
import org.ays.common.model.AysSorting;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.model.dto.request.AysPhoneNumberFilterRequestBuilder;
import org.ays.common.model.request.AysPhoneNumberFilterRequest;
import org.ays.user.model.enums.UserStatus;
import org.ays.user.model.enums.UserSupportStatus;
import org.ays.user.model.request.UserListRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public class UserListRequestBuilder extends TestDataBuilder<UserListRequest> {

    public UserListRequestBuilder() {
        super(UserListRequest.class);
    }

    public UserListRequestBuilder withValidValues() {
        return this
                .withFilter(new FilterBuilder().withValidValues().build())
                .withPagination(new AysPagingBuilder().withValidValues().build())
                .withSort(AysSorting.of(Sort.by(Sort.Direction.ASC, "createdAt")));
    }

    public UserListRequestBuilder withFilter(UserListRequest.Filter filter) {
        data.setFilter(filter);
        return this;
    }

    public UserListRequestBuilder withPagination(AysPaging aysPaging) {
        data.setPagination(aysPaging);
        return this;
    }

    public UserListRequestBuilder withSort(List<AysSorting> sorting) {
        data.setSort(sorting);
        return this;
    }

    public static class FilterBuilder extends TestDataBuilder<UserListRequest.Filter> {

        public FilterBuilder() {
            super(UserListRequest.Filter.class);
        }

        public FilterBuilder withValidValues() {
            return this
                    .withSupportStatuses(List.of(UserSupportStatus.READY, UserSupportStatus.ON_ROAD))
                    .withStatuses(List.of(UserStatus.ACTIVE, UserStatus.DELETED))
                    .withPhoneNumber(new AysPhoneNumberFilterRequestBuilder().withValidValues().build());
        }

        public FilterBuilder withFirstName(String firstName) {
            data.setFirstName(firstName);
            return this;
        }

        public FilterBuilder withLastName(String lastName) {
            data.setLastName(lastName);
            return this;
        }

        public FilterBuilder withSupportStatuses(List<UserSupportStatus> supportStatuses) {
            data.setSupportStatuses(supportStatuses);
            return this;
        }

        public FilterBuilder withStatuses(List<UserStatus> statuses) {
            data.setStatuses(statuses);
            return this;
        }

        public FilterBuilder withPhoneNumber(AysPhoneNumberFilterRequest phoneNumber) {
            data.setPhoneNumber(phoneNumber);
            return this;
        }

    }

}
