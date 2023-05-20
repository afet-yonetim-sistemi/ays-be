package com.ays.common.model;

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

    @SuppressWarnings("unused")
    public static class AysSpecificationBuilder<C> {

        private final Specification<C> specification = Specification.allOf();

        public Specification<C> build() {
            return this.specification;
        }

        public AysSpecificationBuilder<C> and(final Map<String, Object> filter) {

            filter.forEach((name, value) -> {

                Specification<C> tempSpecification = (root, query, criteriaBuilder) -> criteriaBuilder
                        .equal(root.get(name), value);

                this.specification.and(tempSpecification);
            });

            return this;
        }

        public AysSpecificationBuilder<C> or(final Map<String, Object> filter) {

            filter.forEach((name, value) -> {

                Specification<C> tempSpecification = (root, query, criteriaBuilder) -> criteriaBuilder
                        .equal(root.get(name), value);

                this.specification.or(tempSpecification);
            });

            return this;
        }

    }

}
