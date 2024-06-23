package org.ays.common.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * A generic class representing a paginated response containing a list of content items of type R.
 * This class provides convenience methods for building a response from a Spring {@link Page} object.
 *
 * @param <R> The type of content items in this paginated response.
 */
@Getter
@Setter
@Builder
public class AysPage<R> {

    /**
     * The content items in this paginated response.
     */
    private List<R> content;

    /**
     * The current page number.
     */
    private Integer pageNumber;

    /**
     * The page size.
     */
    private Integer pageSize;

    /**
     * The total number of pages.
     */
    private Integer totalPageCount;

    /**
     * The total number of elements.
     */
    private Long totalElementCount;

    /**
     * The sorting parameters used for this paginated response.
     */
    private List<AysSort.AysOrder> orderedBy;

    /**
     * The filtering parameters used for this paginated response.
     */
    private AysFilter filteredBy;

    /**
     * Creates a new paginated response of type C from a Spring {@link Page} object.
     *
     * @param pageableEntities The Spring {@link Page} object containing the page information.
     * @param content          The content items to be included in the response.
     * @param <E>              The type of entities in the Spring {@link Page} object.
     * @param <C>              The type of content items in the response.
     * @return The paginated response.
     */
    public static <E, C> AysPage<C> of(final Page<E> pageableEntities,
                                       final List<C> content) {

        final var responseBuilder = AysPage.<C>builder()
                .content(content)
                .pageNumber(pageableEntities.getNumber() + 1)
                .pageSize(content.size())
                .totalPageCount(pageableEntities.getTotalPages())
                .totalElementCount(pageableEntities.getTotalElements());

        if (pageableEntities.getSort().isSorted()) {
            responseBuilder.orderedBy(AysSort.of(pageableEntities.getSort()).getOrders());
        }

        return responseBuilder.build();
    }

    /**
     * Creates a new paginated response of type C from a Spring {@link Page} object with filtering.
     *
     * @param filter           The filtering parameters to be included in the response.
     * @param pageableEntities The Spring {@link Page} object containing the page information.
     * @param content          The content items to be included in the response.
     * @param <E>              The type of entities in the Spring {@link Page} object.
     * @param <C>              The type of content items in the response.
     * @return The paginated response.
     */
    public static <E, C> AysPage<C> of(final AysFilter filter,
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
            responseBuilder.orderedBy(AysSort.of(pageableEntities.getSort()).getOrders());
        }

        return responseBuilder.build();
    }
}
