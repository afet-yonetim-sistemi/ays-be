package org.ays.common.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Represents a sorting request for a property in a specific direction.
 */
@Getter
@Setter
@Builder
public class AysSorting {

    /**
     * The name of the property to sort by.
     */
    @NotNull
    public String property;

    /**
     * The direction to sort the property in.
     */
    @NotNull
    public Sort.Direction direction;

    /**
     * Converts a Spring Data {@link Sort} object into a list of {@link AysSorting}.
     *
     * @param sort the Spring Data Sort object to convert
     * @return a list of AysSorting objects representing the sort order
     */
    public static List<AysSorting> of(final Sort sort) {
        return sort.stream()
                .map(order -> AysSorting.builder()
                        .property(order.getProperty())
                        .direction(order.getDirection())
                        .build())
                .toList();
    }
}
