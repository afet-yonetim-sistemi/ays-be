package com.ays.common.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@Builder
public class AysSorting {

    @NotNull
    public String property;

    @NotNull
    public Sort.Direction direction;

    public static List<AysSorting> of(final Sort sort) {
        return sort.stream()
                .map(order -> AysSorting.builder()
                        .property(order.getProperty())
                        .direction(order.getDirection())
                        .build())
                .toList();
    }
}
