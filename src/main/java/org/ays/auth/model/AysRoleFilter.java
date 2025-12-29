package org.ays.auth.model;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.ays.auth.model.entity.AysRoleEntity;
import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.common.model.AysFilter;
import org.ays.common.util.validation.RoleNameFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * Encapsulates filter criteria for querying {@link AysRoleEntity}.
 * <p>
 * This class constructs a dynamic JPA {@link Specification} based on fields
 * like role name, status, and institution ID. It ensures that queries are
 * built efficiently and securely.
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

    /**
     * The name of the role to search for.
     * <p>
     * This field supports partial matching and is case-insensitive.
     * Leading and trailing whitespaces are trimmed before the query is constructed.
     * Validated by {@link RoleNameFilter} to ensure it meets the application's naming standards.
     * </p>
     */
    @RoleNameFilter
    @Size(min = 2, max = 255)
    private String name;
    private Set<AysRoleStatus> statuses;
    private String institutionId;

    /**
     * Converts the current filter criteria into a JPA {@link Specification}.
     * <p>
     * This method constructs a dynamic query with the following logic:
     * <ul>
     * <li><b>Institution ID:</b> exact match (Always applied).</li>
     * <li><b>Name:</b> case-insensitive partial match with trimmed input. Applied only if text is present.</li>
     * <li><b>Statuses:</b> efficient clause. Applied only if the set is not empty.</li>
     * </ul>
     * All conditions are combined using the operator.
     * </p>
     *
     * @return a {@link Specification} representing the query criteria.
     */
    @Override
    public Specification<AysRoleEntity> toSpecification() {

        Specification<AysRoleEntity> specification = Specification.unrestricted();

        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("institutionId"), this.institutionId));

        if (StringUtils.hasText(this.name)) {
            String searchKey = this.name.trim();

            if (StringUtils.hasText(searchKey)) {
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.like(
                                criteriaBuilder.upper(root.get("name")),
                                "%" + searchKey.toUpperCase() + "%"
                        ));
            }
        }

        if (!CollectionUtils.isEmpty(this.statuses)) {
            Specification<AysRoleEntity> statusSpecification = (root, query, criteriaBuilder) ->
                    root.get("status").in(this.statuses);

            specification = specification.and(statusSpecification);
        }

        return specification;
    }

}
