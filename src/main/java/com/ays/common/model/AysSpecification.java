package com.ays.common.model;

import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AysSpecification {

    /**
     * A builder class for creating specifications in the context of AysSpecification.
     * Use this builder to construct predicates based on a provided filter.
     */
    @SuppressWarnings("This method is unused by the application directly but Spring is using it in the background.")
    public static class AysSpecificationBuilder<C> {

        /**
         * Constructs a Specification by applying the provided filter as an AND condition.
         *
         * @param filter a map representing the filter conditions, where the key is the field name and the value is the desired value
         * @return a Specification representing the AND condition of the provided filter
         */
        public Specification<C> and(final Map<String, Object> filter) {

            final Predicate[] predicates = new Predicate[filter.size()];
            final String[] names = filter.keySet().toArray(new String[0]);

            return ((root, query, criteriaBuilder) -> {

                for (int count = 0; count < filter.size(); count++) {

                    final String name = names[count];
                    final String value = filter.get(name).toString();

                    predicates[count] = criteriaBuilder.equal(root.get(name), value);
                }
                return criteriaBuilder.and(predicates);
            });
        }

    }

}
