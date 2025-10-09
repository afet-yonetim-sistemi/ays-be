package org.ays.auth.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.ays.auth.model.entity.AdminRegistrationApplicationEntity;
import org.ays.auth.model.enums.AdminRegistrationApplicationStatus;
import org.ays.common.model.AysFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

/**
 * Represents a filter for querying admin registration applications based on specified statuses.
 * This class implements {@link AysFilter} to provide filtering capabilities for admin registration applications.
 */
@Getter
@Setter
public class AdminRegistrationApplicationFilter implements AysFilter {

    private Set<AdminRegistrationApplicationStatus> statuses;

    /**
     * Converts this filter configuration into a {@link Specification} for querying {@link AdminRegistrationApplicationEntity}.
     * If statuses are specified, generates a specification that filters by each status using an OR condition.
     *
     * @return A {@link Specification} that encapsulates the filter criteria.
     */
    @Override
    public Specification<AdminRegistrationApplicationEntity> toSpecification() {

        Specification<AdminRegistrationApplicationEntity> specification = Specification.unrestricted();

        if (!CollectionUtils.isEmpty(this.statuses)) {
            Specification<AdminRegistrationApplicationEntity> statusSpecification = this.statuses.stream()
                    .map(status -> (Specification<AdminRegistrationApplicationEntity>) (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get("status"), status))
                    .reduce(Specification::or)
                    .orElse(null);

            specification = specification.and(statusSpecification);
        }

        return specification;

    }

}
