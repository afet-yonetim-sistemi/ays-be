package org.ays.common.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Represents a custom sorting configuration that integrates with Spring Data's sorting functionality.
 * This class wraps a list of {@link AysOrder} instances, each specifying a property and direction for sorting.
 * <p>
 * It provides conversion methods to and from Spring Data's {@link org.springframework.data.domain.Sort}.
 * </p>
 *
 * @see org.springframework.data.domain.Sort
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AysSort {

    /**
     * A list of sorting orders, each defining a property and direction.
     */
    @Valid
    protected List<AysOrder> orders;

    /**
     * Represents an individual sorting order consisting of a property and a direction.
     * <p>
     * Each instance defines a property to sort by and the direction of sorting (ascending or descending).
     * </p>
     */
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AysOrder {

        @NotNull
        private String property;

        @NotNull
        private Direction direction;

    }

    /**
     * Enum representing the direction of sorting: ascending or descending.
     * <p>
     * Provides a method to convert to Spring Data's {@link org.springframework.data.domain.Sort.Direction}.
     * </p>
     */
    public enum Direction {

        ASC,
        DESC;

        /**
         * Converts this enum to the corresponding {@link org.springframework.data.domain.Sort.Direction}.
         *
         * @return the corresponding {@link org.springframework.data.domain.Sort.Direction}
         */
        public Sort.Direction toDirection() {
            return Sort.Direction.valueOf(this.name());
        }
    }

    /**
     * Converts this {@link AysSort} instance to a {@link org.springframework.data.domain.Sort} instance.
     * <p>
     * This method maps each {@link AysOrder} to a {@link Sort.Order} and returns a {@link Sort} object.
     * </p>
     *
     * @return a {@link org.springframework.data.domain.Sort} representing the same sorting configuration
     */
    protected org.springframework.data.domain.Sort toSort() {
        final List<Sort.Order> orders = this.orders.stream()
                .map(order -> Sort.Order.by(order.getProperty()).with(order.getDirection().toDirection()))
                .toList();
        return org.springframework.data.domain.Sort.by(orders);
    }


    /**
     * Creates an {@link AysSort} instance from a given {@link org.springframework.data.domain.Sort} object.
     * <p>
     * This method converts each {@link Sort.Order} in the input into an {@link AysOrder} and constructs an {@link AysSort} object.
     * </p>
     *
     * @param sorts the {@link org.springframework.data.domain.Sort} to convert
     * @return a new {@link AysSort} object representing the same sorting configuration
     */
    public static AysSort of(final org.springframework.data.domain.Sort sorts) {

        List<AysOrder> orders = sorts.stream()
                .map(order -> AysOrder.builder()
                        .property(order.getProperty())
                        .direction(Direction.valueOf(order.getDirection().toString()))
                        .build())
                .toList();

        return AysSort.builder()
                .orders(orders)
                .build();
    }

}
