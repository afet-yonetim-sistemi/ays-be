package org.ays.common.model;

import org.springframework.data.jpa.domain.Specification;

/**
 * Interface for defining filter criteria in AYS application services.
 * <p>
 * Implementing this interface allows for the conversion of filter criteria into a JPA {@link Specification}.
 * This can be used to dynamically generate query conditions for entities in a type-safe manner, supporting
 * flexible and reusable query construction.
 * </p>
 * <p>
 * Each implementation should provide logic to convert its filter settings into a {@link Specification}
 * that can be applied to JPA repository queries. This approach is beneficial for creating dynamic,
 * database-independent filters.
 * </p>
 *
 * <h3>Example Usage</h3>
 * <pre>{@code
 * public class UserFilter implements AysFilter {
 *     private String username;
 *     private LocalDate registrationDate;
 *
 *     {@literal @}Override
 *     public Specification<UserEntity> toSpecification() {
 *         return (root, query, criteriaBuilder) -> {
 *             List<Predicate> predicates = new ArrayList<>();
 *             if (username != null) {
 *                 predicates.add(criteriaBuilder.equal(root.get("username"), username));
 *             }
 *             if (registrationDate != null) {
 *                 predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("registrationDate"), registrationDate));
 *             }
 *             return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
 *         };
 *     }
 * }
 * }</pre>
 * <p>
 * In this example, `UserFilter` converts its fields into a {@link Specification} used to filter `UserEntity`
 * based on the provided `username` and `registrationDate`.
 * </p>
 *
 * @param <T> the type of the entity to which the filter specification will be applied.
 */
public interface AysFilter {

    /**
     * Converts this filter's configuration into a {@link Specification} for querying.
     *
     * @return A {@link Specification} object representing the query criteria defined by this filter.
     */
    @SuppressWarnings({"java:S3740", "rawtypes"})
    Specification toSpecification();

}
