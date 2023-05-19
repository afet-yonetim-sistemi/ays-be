package com.ays.admin_user.repository.specification;

import com.ays.admin_user.model.entity.AdminUserEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

/**
 * AdminUserEntityHasOrganizationIdSpecification for implementing specification by organization id
 */
class AdminUserEntityHasOrganizationIdSpecification implements Specification<AdminUserEntity> {
    private final String organizationId;

    public AdminUserEntityHasOrganizationIdSpecification(String organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public Predicate toPredicate(Root<AdminUserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return SearchSpecificationBuilder.eq(criteriaBuilder, root.get("organizationId"), organizationId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminUserEntityHasOrganizationIdSpecification that = (AdminUserEntityHasOrganizationIdSpecification) o;
        return Objects.equals(organizationId, that.organizationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizationId);
    }
}
