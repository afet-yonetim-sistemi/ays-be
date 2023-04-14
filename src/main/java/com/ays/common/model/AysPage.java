package com.ays.common.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class AysPage<R> {

    private List<R> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPageCount;
    private Long totalElementCount;
    private List<AysSorting> sortedBy;
    private AysFiltering filteredBy;

    public static <E, C> AysPage<C> of(final Page<E> pageableEntities,
                                       final List<C> content) {

        final var responseBuilder = AysPage.<C>builder()
                .content(content)
                .pageNumber(pageableEntities.getNumber() + 1)
                .pageSize(content.size())
                .totalPageCount(pageableEntities.getTotalPages())
                .totalElementCount(pageableEntities.getTotalElements());

        if (pageableEntities.getSort().isSorted()) {
            responseBuilder.sortedBy(AysSorting.of(pageableEntities.getSort()));
        }

        return responseBuilder.build();
    }

    public static <E, C> AysPage<C> of(final AysFiltering filter,
                                       final Page<E> pageableEntities,
                                       final List<C> content) {

        final var responseBuilder = AysPage.<C>builder()
                .content(content)
                .pageNumber(pageableEntities.getNumber() + 1)
                .pageSize(content.size())
                .totalPageCount(pageableEntities.getTotalPages())
                .totalElementCount(pageableEntities.getTotalElements())
                .filteredBy(filter);

        if (pageableEntities.getSort().isSorted()) {
            responseBuilder.sortedBy(AysSorting.of(pageableEntities.getSort()));
        }

        return responseBuilder.build();
    }
}
