package org.ays.common.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ays.common.model.AysPageable;
import org.ays.common.model.AysSort;

import java.util.List;
import java.util.Set;

/**
 * Represents a base class for paging requests, providing common functionality for handling pageable data.
 * <p>
 * This class contains a pageable attribute and provides methods to validate if the sort properties used in the pageable are accepted.
 * Subclasses should implement {@link #isOrderPropertyAccepted()} to provide specific validation logic.
 * </p>
 *
 * <h3>Example Usage</h3>
 *
 * <pre>{@code
 * public class Request extends AysPagingRequest {
 *     @Override
 *     public boolean isSortPropertyAccepted() {
 *          final Set<String> acceptedFilterFields = Set.of("createdAt");
 *          return this.isPropertyAccepted(acceptedFilterFields);
 *     }
 * }
 * }</pre>
 *
 * @see AysPageable
 * @see AysSort.AysOrder
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class AysPagingRequest {

    /**
     * The pageable object containing paging and sorting information.
     * <p>
     * Must not be null and is validated using {@link jakarta.validation.Valid}.
     * </p>
     */
    @Valid
    @NotNull
    protected AysPageable pageable;

    /**
     * Checks if the sort properties in the pageable are accepted.
     * <p>
     * This method should be implemented by subclasses to provide specific validation logic for sort properties.
     * </p>
     *
     * @return {@code true} if the sort properties are accepted; otherwise {@code false}
     */
    public abstract boolean isOrderPropertyAccepted();

    /**
     * Validates if all properties used for sorting in the pageable object are within the accepted set of properties.
     * <p>
     * Checks if there are any orders with null direction or property. If so, it returns {@code true}.
     * Otherwise, it ensures all properties in the pageable orders are in the accepted properties set.
     * </p>
     *
     * @param acceptedProperties the set of accepted properties for sorting
     * @return {@code true} if all properties are accepted or if pageable or its orders are null/empty; otherwise {@code false}
     */
    @SuppressWarnings("all")
    public boolean isPropertyAccepted(final Set<String> acceptedProperties) {

        if (this.pageable == null || CollectionUtils.isEmpty(this.pageable.getOrders())) {
            return true;
        }

        for (AysSort.AysOrder order : this.pageable.getOrders()) {
            if (StringUtils.isBlank(order.getProperty()) || order.getDirection() == null) {
                return true;
            }
        }

        List<AysSort.AysOrder> orders = this.pageable.getOrders();
        if (CollectionUtils.isEmpty(orders)) {
            return true;
        }

        final boolean isAnyDirectionEmpty = orders.stream().anyMatch(order -> order.getDirection() == null);
        final boolean isAnyPropertyEmpty = orders.stream().anyMatch(order -> order.getProperty() == null);
        if (isAnyDirectionEmpty || isAnyPropertyEmpty) {
            return true;
        }

        return orders.stream()
                .map(AysSort.AysOrder::getProperty)
                .allMatch(acceptedProperties::contains);
    }

}
