package com.ays.admin_user.repository.specification;

import com.ays.admin_user.model.entity.AdminUserEntity;
import org.springframework.data.jpa.domain.Specification;

public class AdminUserSpecifications {

    public static Specification<AdminUserEntity> hasOrganizationId(String organizationId) {
        return (root, query, criteriaBuilder) ->
                SearchSpecificationBuilder.eq(criteriaBuilder, root.get("organizationId"), organizationId);
    }
}
