package com.ays.admin_user.repository.specification;

import com.ays.admin_user.model.entity.AdminUserEntity;
import org.springframework.data.jpa.domain.Specification;

/**
 * AdminUserSpecifications for handling with getting all list with respect to the defined criteria
 */
public class AdminUserSpecifications {

    /**
     * hasOrganizationId method for getting all admin list by organization Id
     *
     * @param organizationId the organization identifier
     */
    public static Specification<AdminUserEntity> hasOrganizationId(String organizationId) {
        return (root, query, criteriaBuilder) ->
                SearchSpecificationBuilder.eq(criteriaBuilder, root.get("organizationId"), organizationId);
    }
}
