package com.ays.common.model;

import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AysSpecification {

    /**
     * A builder class for creating specifications in the context of AysSpecification.
     * Use this builder to construct predicates based on a provided filter.
     */
    @SuppressWarnings("unused")
    public static class AysSpecificationBuilder<C> {

        /**
         * Constructs a Specification by applying the provided filter as an AND condition.
         *
         * @param filter a map representing the filter conditions, where the key is the field name and the value is the desired value
         * @return a Specification representing the AND condition of the provided filter
         */
        public Specification<C> and(final Map<String, Object> filter) {

            List<Predicate> predicates = new ArrayList<>();
            return ((root, query, criteriaBuilder) -> {
                filter.forEach((name, value) -> {
                    Predicate equal = criteriaBuilder.equal(root.get(name), value);
                    predicates.add(equal);
                });
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            });
        }

    }

}
