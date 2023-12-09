package com.ays.user.model.dto.request;

import com.ays.common.model.AysFiltering;
import com.ays.common.model.AysPhoneNumberFilterRequest;
import com.ays.common.model.dto.request.AysFilteringRequest;
import com.ays.common.model.dto.request.AysPagingRequest;
import com.ays.common.util.validation.Name;
import com.ays.user.model.enums.UserStatus;
import com.ays.user.model.enums.UserSupportStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * Represents a request object for fetching a list of users with pagination and sorting options.
 * This class extends the {@link AysPagingRequest} class and adds additional validation rules for sorting.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class UserListRequest extends AysPagingRequest implements AysFilteringRequest {

    @Valid
    private Filter filter;

    /**
     * Represents a filter request object for filtering the list of users.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Filter implements AysFiltering {

        @Name
        private String firstName;

        @Name
        private String lastName;

        private List<UserSupportStatus> supportStatuses;

        private List<UserStatus> statuses;

        @Valid
        private AysPhoneNumberFilterRequest phoneNumber;

    }

    /**
     * Overrides the {@link AysPagingRequest#isSortPropertyAccepted()} method to validate sorting options
     * and ensures that no unsupported sorting property is used in the request.
     *
     * @return true if the sorting property is accepted, false otherwise.
     */
    @JsonIgnore
    @AssertTrue
    @Override
    public boolean isSortPropertyAccepted() {
        final Set<String> acceptedFilterFields = Set.of("createdAt");
        return this.isPropertyAccepted(acceptedFilterFields);
    }

    @Override
    public <C> Specification<C> toSpecification(Class<C> clazz) {

        if (this.filter == null) {
            return Specification.allOf();
        }

        Specification<C> specification = Specification.where(null);

        if (StringUtils.hasText(this.filter.firstName)) {
            Specification<C> firstNameSpecification = (root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("firstName"), this.filter.firstName);
            specification = specification.and(firstNameSpecification);
        }

        if (StringUtils.hasText(this.filter.lastName)) {
            Specification<C> lastNameSpecification = (root, query, criteriaBuilder)
                    -> criteriaBuilder.like(root.get("lastName"), this.filter.lastName);
            specification = specification.and(lastNameSpecification);
        }

        if (!CollectionUtils.isEmpty(this.filter.supportStatuses)) {
            Specification<C> supportStatusSpecification = (root, query, criteriaBuilder) ->
                    root.get("supportStatus").in(this.filter.supportStatuses);
            specification = specification.and(supportStatusSpecification);
        }

        if (!CollectionUtils.isEmpty(this.filter.statuses)) {
            Specification<C> userStatusSpecification = (root, query, criteriaBuilder) ->
                    root.get("status").in(this.filter.statuses);
            specification = specification.and(userStatusSpecification);
        }

        if (this.filter.phoneNumber != null) {

            if (StringUtils.hasText(this.filter.phoneNumber.getLineNumber())) {
                Specification<C> lineNumberSpecification = (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("lineNumber"), this.filter.phoneNumber.getLineNumber());

                specification = specification.and(lineNumberSpecification);
            }

            if (StringUtils.hasText(this.filter.phoneNumber.getCountryCode())) {
                Specification<C> countryCodeSpecification = (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get("countryCode"), this.filter.phoneNumber.getCountryCode());

                specification = specification.and(countryCodeSpecification);
            }
        }

        return specification;
    }
}
