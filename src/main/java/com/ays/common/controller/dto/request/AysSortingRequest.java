package com.ays.common.controller.dto.request;

import com.ays.common.model.AysSorting;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Set;

@Data
public abstract class AysSortingRequest {

    @Valid
    protected List<AysSorting> sort;

    public abstract boolean isSortPropertyAccepted();

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

    protected boolean isSortable() {
        return this.sort != null;
    }

    public Sort toSort() {
        return Sort.by(this.sort.stream()
                .map(sortable -> Sort.Order.by(sortable.getProperty()).with(sortable.getDirection()))
                .toList());
    }
}
