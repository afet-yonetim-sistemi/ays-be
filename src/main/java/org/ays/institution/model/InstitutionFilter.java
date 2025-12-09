package org.ays.institution.model;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.ays.common.model.AysFilter;
import org.ays.common.util.validation.Name;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.enums.InstitutionStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

/**
 * Filter criteria for querying institutions in the application.
 * <p>
 * The {@link InstitutionFilter} class allows the construction of dynamic query specifications
 * for institutions, based on the provided filter parameters. It implements the {@link AysFilter} interface,
 * which requires the implementation of the {@link InstitutionFilter#toSpecification()} method.
 * </p>
 * <p>
 * When converting to a {@link Specification}, it combines the criteria using logical AND operations.
 * </p>
 */
@Getter
@Setter
@Builder
public class InstitutionFilter implements AysFilter {

    @Name
    @Size(min = 1, max = 255)
    private String name;

    private Set<InstitutionStatus> statuses;

    /**
     * Converts the current filter criteria into a {@link Specification} for querying institutions.
     * <p>
     * This method builds a {@link Specification} based on the filter properties. It uses
     * the name for partial matching (case-insensitive), filters by the provided
     * institution statuses. If no specific filter properties are specified,
     * those criteria are not included in the final specification.
     * </p>
     *
     * @return a {@link Specification} object representing the query criteria based on the current filter.
     */
    @Override
    public Specification<InstitutionEntity> toSpecification() {

        Specification<InstitutionEntity> specification = Specification.unrestricted();

        if (!CollectionUtils.isEmpty(this.statuses)) {

            Specification<InstitutionEntity> statusSpecification = this.statuses.stream()
                    .map(status -> (Specification<InstitutionEntity>) (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get("status"), status))
                    .reduce(Specification::or)
                    .orElse(null);

            specification = specification.and(statusSpecification);
        }

        if (this.name != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + this.name.toUpperCase() + "%"));
        }

        return specification;
    }

}

