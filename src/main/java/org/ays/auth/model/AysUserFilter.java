package org.ays.auth.model;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.ays.auth.model.entity.AysUserEntity;
import org.ays.auth.model.enums.AysUserStatus;
import org.ays.common.model.AysFilter;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.util.validation.Name;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * Filter criteria for querying users in the application.
 * <p>
 * The {@link AysUserFilter} class allows the construction of dynamic query specifications
 * for users, based on the provided filter parameters. It implements the {@link AysFilter} interface,
 * which requires the implementation of the {@link AysUserFilter#toSpecification()} method.
 * </p>
 * <p>
 * This filter supports filtering by first name, last name, phone number, statuses, city, and institution ID.
 * When converting to a {@link Specification}, it combines the criteria using logical AND operations.
 * </p>
 *
 * @see AysUserEntity
 * @see AysFilter
 * @see org.springframework.data.jpa.domain.Specification
 */
@Getter
@Setter
@Builder
public class AysUserFilter implements AysFilter {

    @Name
    @Size(min = 2, max = 255)
    private String firstName;

    @Name
    @Size(min = 2, max = 255)
    private String lastName;

    private AysPhoneNumber phoneNumber;

    private Set<AysUserStatus> statuses;

    @Name
    @Size(min = 2, max = 100)
    private String city;

    private String institutionId;


    /**
     * Converts the current filter criteria into a {@link Specification} for querying users.
     * <p>
     * This method builds a {@link Specification} based on the filter properties. It uses
     * the first name and last name for partial matching (case-insensitive), filters by the provided
     * user statuses, phone number, city, and institution ID. If no specific filter properties are specified,
     * those criteria are not included in the final specification.
     * </p>
     *
     * @return a {@link Specification} object representing the query criteria based on the current filter.
     */
    @Override
    public Specification<AysUserEntity> toSpecification() {

        Specification<AysUserEntity> specification = Specification.where(null);

        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("institutionId"), this.institutionId));

        if (!CollectionUtils.isEmpty(this.statuses)) {
            Specification<AysUserEntity> statusSpecification = this.statuses.stream()
                    .map(status -> (Specification<AysUserEntity>) (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get("status"), status))
                    .reduce(Specification::or)
                    .orElse(null);
            specification = specification.and(statusSpecification);
        }

        if (this.firstName != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + this.firstName.toLowerCase() + "%"));
        }

        if (this.lastName != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + this.lastName.toLowerCase() + "%"));
        }

        if (this.phoneNumber != null && StringUtils.hasText(this.phoneNumber.getCountryCode())) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("countryCode"), "%" + this.phoneNumber.getCountryCode() + "%"));
        }

        if (this.phoneNumber != null && StringUtils.hasText(this.phoneNumber.getLineNumber())) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("lineNumber"), "%" + this.phoneNumber.getLineNumber() + "%"));
        }

        if (this.city != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("city")), "%" + this.city.toLowerCase() + "%"));
        }

        return specification;

    }

}

