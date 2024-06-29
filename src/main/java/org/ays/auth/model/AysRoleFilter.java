package org.ays.auth.model;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.ays.auth.model.entity.AysRoleEntity;
import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.common.model.AysFilter;
import org.ays.common.util.validation.Name;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

/**
 * Filter criteria for querying roles in the application.
 * <p>
 * The {@link AysRoleFilter} class allows the construction of dynamic query specifications
 * for roles, based on the provided filter parameters. It implements the {@link AysFilter} interface,
 * which requires the implementation of the {@link AysRoleFilter#toSpecification()} method.
 * </p>
 * <p>
 * This filter supports filtering by role name and statuses. When converting to a
 * {@link Specification}, it combines the criteria for name and statuses using logical AND operations.
 * </p>
 *
 * @see AysRoleEntity
 * @see AysFilter
 * @see org.springframework.data.jpa.domain.Specification
 */
@Getter
@Setter
@Builder
public class AysRoleFilter implements AysFilter {

    @Name
    @Size(min = 2, max = 255)
    private String name;
    private Set<AysRoleStatus> statuses;
    private String institutionId;

    /**
     * Converts the current filter criteria into a {@link Specification} for querying roles.
     * <p>
     * This method builds a {@link Specification} based on the filter properties. It uses
     * the role name for partial matching (case-insensitive) and filters by the provided
     * role statuses. If no statuses are specified, this criterion is not included in the
     * final specification.
     * </p>
     *
     * @return a {@link Specification} object representing the query criteria based on the current filter.
     */
    @Override
    public Specification<AysRoleEntity> toSpecification() {

        Specification<AysRoleEntity> specification = Specification.where(null);

        specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("institutionId"), this.institutionId));

        if (this.name != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + this.name.toLowerCase() + "%"));
        }

        if (!CollectionUtils.isEmpty(this.statuses)) {
            Specification<AysRoleEntity> statusSpecification = this.statuses.stream().map(status -> (Specification<AysRoleEntity>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status)).reduce(Specification::or).orElse(null);

            specification = specification.and(statusSpecification);
        }

        return specification;
    }

}
