package com.ays.admin_user.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import java.util.Locale;

/**
 * SearchSpecificationBuilder for handling with Specification operations
 */
public class SearchSpecificationBuilder {

    /**
     * or operation for or specification
     */
    public static Predicate or(CriteriaBuilder criteriaBuilder, Predicate predicate1, Predicate predicate2) {
        if (predicate2 == null && predicate1 == null) {
            return null;
        }
        if (predicate2 != null && predicate1 == null) {
            return predicate2;
        }
        if (predicate2 == null && predicate1 != null) {
            return predicate1;
        }

        return criteriaBuilder.or(predicate1, predicate2);
    }

    /**
     * and operation for and specification
     */
    public static Predicate and(CriteriaBuilder criteriaBuilder, Predicate predicate1, Predicate predicate2) {
        if (predicate2 == null && predicate1 == null) {
            return null;
        }
        if (predicate2 != null && predicate1 == null) {
            return predicate2;
        }
        if (predicate2 == null && predicate1 != null) {
            return predicate1;
        }

        return criteriaBuilder.and(predicate1, predicate2);
    }

    /**
     * like operation for like specification
     */
    public static Predicate like(CriteriaBuilder builder, Path<String> path, String value) {
        if (value == null) {
            return null;
        }
        value = value.toLowerCase(Locale.forLanguageTag("tr"));
        String[] valueList = value.split(" ");
        StringBuilder stringBuilder = new StringBuilder();

        if (valueList.length > 1) {
            for (String search : valueList) {
                stringBuilder.append("%").append(search).append("%");
            }
            return builder.like(builder.lower(path), String.valueOf(stringBuilder));
        }
        return builder.like(builder.lower(path), "%" + value + "%");
    }

    /**
     * eq operation for equal specification
     */
    public static Predicate eq(CriteriaBuilder criteriaBuilder, Path<Object> path, Object value) {
        if (value == null) {
            return null;
        }
        return criteriaBuilder.equal(path, value);
    }

    /**
     * ne operation for not equal specification
     */
    public static Predicate ne(CriteriaBuilder criteriaBuilder, Path<Object> path, Object value) {
        if (value == null) {
            return null;
        }
        return criteriaBuilder.notEqual(path, value);
    }


    /**
     * isNull operation for is null specification
     */
    public static Predicate isNull(CriteriaBuilder criteriaBuilder, Path<Object> path) {
        return criteriaBuilder.isNull(path);
    }

    /**
     * isNotNull operation for is not null specification
     */
    public static Predicate isNotNull(CriteriaBuilder criteriaBuilder, Path<Object> path) {
        return criteriaBuilder.isNotNull(path);
    }

    /**
     * in operation for in specification
     */
    public static Predicate in(CriteriaBuilder criteriaBuilder, Path<Object> path, Object[] value) {
        if (value == null) {
            return null;
        }
        return criteriaBuilder.in(path).value(value);
    }

    /**
     * notIn operation for not in specification
     */
    public static Predicate notIn(CriteriaBuilder criteriaBuilder, Path<Object> path, Object[] value) {
        if (value == null) {
            return null;
        }
        return criteriaBuilder.not(criteriaBuilder.in(path).value(value));
    }

}
