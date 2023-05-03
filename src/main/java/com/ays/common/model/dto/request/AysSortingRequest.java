package com.ays.common.model.dto.request;

import com.ays.common.model.AysSorting;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Set;

/**
 * An abstract base class for sorting requests that defines a list of {@link AysSorting} objects to
 * specify sorting parameters. It provides methods to check if a given property is accepted for sorting,
 * and to convert the sorting parameters to a Spring {@link Sort} object.
 */
@Data
public abstract class AysSortingRequest {

    /**
     * The sorting parameters for this request.
     */
    @Valid
    protected List<AysSorting> sort;

    /**
     * Determines whether the sorting property for this request is accepted.
     *
     * @return True if the sorting property is accepted, false otherwise.
     */
    public abstract boolean isSortPropertyAccepted();

    /**
     * Determines whether a given property is accepted for sorting, based on a set of accepted properties.
     *
     * @param acceptedProperties The set of accepted properties.
     * @return True if the given property is accepted, false otherwise.
     */
    protected boolean isPropertyAccepted(final Set<String> acceptedProperties) {

        if (this.sort == null || this.sort.isEmpty()) {
            return true;
        }

        final boolean isAnyDirectionEmpty = this.sort.stream().anyMatch(sortable -> sortable.getDirection() == null);
        final boolean isAnyPropertyEmpty = this.sort.stream().anyMatch(sortable -> sortable.getProperty() == null);
        if (isAnyDirectionEmpty || isAnyPropertyEmpty) {
            return true;
        }

        return sort.stream()
                .map(AysSorting::getProperty)
                .allMatch(acceptedProperties::contains);
    }

    /**
     * Determines whether this request is sortable.
     *
     * @return True if this request is sortable, false otherwise.
     */
    protected boolean isSortable() {
        return this.sort != null;
    }

    /**
     * Converts the sorting parameters of this request to a Spring {@link Sort} object.
     *
     * @return The {@link Sort} object corresponding to this request's sorting parameters.
     */
    public Sort toSort() {
        return Sort.by(this.sort.stream()
                .map(sortable -> Sort.Order.by(sortable.getProperty()).with(sortable.getDirection()))
                .toList());
    }
}
