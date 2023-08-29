package com.ays.assignment.model.dto.request;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.model.AysFiltering;
import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.dto.request.AysFilteringRequest;
import com.ays.common.model.dto.request.AysPagingRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;

/**
 * Represents a request object for fetching a list of user assignment with pagination,sorting and filtering options
 * This class extends the {@link AysPagingRequest} class and adds additional validation rules for sorting.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentListRequest extends AysPagingRequest implements AysFilteringRequest {

    @Valid
    private Filter filter;

    /**
     * Represents a filtering configuration for assignments based on the class fields.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Filter implements AysFiltering {


        /**
         * List of assignment statuses used for filtering.
         */
        private List<AssignmentStatus> statuses;

        private AysPhoneNumber phoneNumber;
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
        final Set<String> acceptedFilterFields = Set.of();
        return this.isPropertyAccepted(acceptedFilterFields);
    }


    /**
     * Converts the request into a JPA Specification that filters assignments based on the specified
     * statuses and phoneNumber, if they are provided.
     *
     * @param clazz the class type of the specification.
     * @return the generated JPA Specification based on the request filters.
     */
    @Override
    public <C> Specification<C> toSpecification(Class<C> clazz) {

        if (this.filter == null) {
            return Specification.allOf();
        }

        Specification<C> specification = Specification.where(null);

        if (this.filter.phoneNumber != null) {

            if (this.filter.phoneNumber.getLineNumber() != null) {
                Specification<C> lineNumberSpecification = (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("lineNumber"), this.filter.phoneNumber.getLineNumber());

                specification = specification.and(lineNumberSpecification);
            }

            if (this.filter.phoneNumber.getCountryCode() != null) {
                Specification<C> countryCodeSpecification = (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get("countryCode"), this.filter.phoneNumber.getCountryCode());

                specification = specification.and(countryCodeSpecification);
            }
        }

        if (this.filter.statuses != null) {
            Specification<C> statusSpecification = this.filter.statuses.stream().map(status ->
                            (Specification<C>) (root, query, criteriaBuilder) ->
                                    criteriaBuilder.equal(root.get("status"), status))
                    .reduce(Specification::or).orElse(null);

            specification = specification.and(statusSpecification);
        }

        return specification;
    }

}
