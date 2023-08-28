package com.ays.assignment.model.dto.request;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.model.AysFiltering;
import com.ays.common.model.dto.request.AysFilteringRequest;
import com.ays.common.model.dto.request.AysPagingRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;

/**
 * Represents a request object for fetching a list of user assignment with pagination,sorting and filtering options
 * This class extends the {@link AysPagingRequest} class and adds additional validation rules for sorting.
 */
@Data
@Builder
public class AssignmentListRequest extends AysPagingRequest implements AysFilteringRequest {

    @Valid
    public Filter filter;

    /**
     * Represents a filtering configuration for assignments based on the class fields.
     */
    @Data
    public static class Filter implements AysFiltering {


        /**
         * List of assignment statuses used for filtering.
         */
        @NotNull
        public List<AssignmentStatus> statuses;

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
     * Converts the request into a JPA Specification that filters assignments based on the specified statuses,
     * if they are provided.
     *
     * @param clazz the class type of the specification.
     * @return the generated JPA Specification based on the request filters.
     */
    @Override
    public <C> Specification<C> toSpecification(Class<C> clazz) {
        if (this.filter == null) {
            return Specification.allOf();
        }

        return this.filter.statuses.stream()
                .map(status -> (Specification<C>)
                        (root, query, criteriaBuilder) -> criteriaBuilder
                                .equal(root.get("status"), status))
                .reduce(Specification::or)
                .orElse(null);
    }

}
