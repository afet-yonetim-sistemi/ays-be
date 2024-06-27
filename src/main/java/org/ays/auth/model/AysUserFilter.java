package org.ays.auth.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.ays.auth.model.entity.AysUserEntity;
import org.ays.auth.model.enums.AysUserStatus;
import org.ays.common.model.AysFilter;
import org.ays.common.model.AysPhoneNumber;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Set;

// todo javadoc

@Getter
@Setter
@Builder
public class AysUserFilter implements AysFilter {

    private String firstName;
    private String lastName;
    private Set<AysUserStatus> statuses;
    private AysPhoneNumber phoneNumber;

    @Override
    public Specification<AysUserEntity> toSpecification() {

        Specification<AysUserEntity> specification = Specification.where(null);

        if (!CollectionUtils.isEmpty(this.statuses)) {
            Specification<AysUserEntity> statusSpecification = this.statuses.stream()
                    .map(status -> (Specification<AysUserEntity>) (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get("status"), status))
                    .reduce(Specification::or)
                    .orElse(null);

            specification = specification.and(statusSpecification);
        }

        if (StringUtils.hasText(this.firstName)) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + this.lastName.toLowerCase() + "%"));

        }
        if (StringUtils.hasText(this.lastName)) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + this.lastName.toLowerCase() + "%"));

        }
        if (StringUtils.hasText(this.phoneNumber.getCountryCode())) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("countryCode"), "%" + this.phoneNumber.getCountryCode() + "%"));

        }
        if (StringUtils.hasText(this.phoneNumber.getLineNumber())) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("lineNumber"), "%" + this.phoneNumber.getLineNumber() + "%"));

        }
        return specification;

    }

}

