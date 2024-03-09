package org.ays.admin_user.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import org.ays.common.model.AysFiltering;
import org.ays.common.model.dto.request.AysFilteringRequest;
import org.ays.common.model.dto.request.AysPagingRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;

/**
 * Represents a request object for fetching a list of admin user registration applications with pagination,sorting
 * and filtering options. This class extends the {@link AysPagingRequest} class and adds additional validation rules for sorting.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class AdminUserRegisterApplicationListRequest extends AysPagingRequest implements AysFilteringRequest {

    @Valid
    private Filter filter;

    /**
     * Represents a filtering configuration for admin user registration applications based on the class fields.
     */
    @Getter
    @Setter
    public static class Filter implements AysFiltering {


        /**
         * List of admin user registration application's statuses used for filtering.
         */
        private List<AdminUserRegisterApplicationStatus> statuses;

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

    /**
     * Converts the request into a JPA Specification that filters registration applications based on the specified
     * statuses, if they are provided.
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

        if (!CollectionUtils.isEmpty(this.filter.getStatuses())) {
            Specification<C> statusSpecification = this.filter.statuses.stream().map(status ->
                            (Specification<C>) (root, query, criteriaBuilder) ->
                                    criteriaBuilder.equal(root.get("status"), status))
                    .reduce(Specification::or).orElse(null);

            specification = specification.and(statusSpecification);
        }

        return specification;
    }

}
