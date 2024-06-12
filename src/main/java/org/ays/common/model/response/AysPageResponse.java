package org.ays.common.model.response;

import lombok.Builder;
import lombok.Getter;
import org.ays.common.model.AysFiltering;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysSorting;

import java.util.List;

/**
 * A generic response class representing a paginated result.
 *
 * @param <R> The type of content in the response.
 */
@Getter
@Builder
public class AysPageResponse<R> {

    /**
     * The list of content elements in the response.
     */
    private List<R> content;

    /**
     * The current page number.
     */
    private Integer pageNumber;

    /**
     * The number of elements per page.
     */
    private Integer pageSize;

    /**
     * The total number of pages.
     */
    private Integer totalPageCount;

    /**
     * The total number of elements across all pages.
     */
    private Long totalElementCount;

    /**
     * The list of sorting criteria applied to the results.
     */
    private List<AysSorting> sortedBy;

    /**
     * The filtering criteria applied to the results.
     */
    private AysFiltering filteredBy;


    /**
     * Builder class for constructing instances of {@link AysPageResponse}.
     *
     * @param <R> The type of content in the response.
     */
    @SuppressWarnings("This method is unused by the application directly but Spring is using it in the background.")
    public static class AysPageResponseBuilder<R> {

        /**
         * Creates an instance of {@link AysPageResponseBuilder} from an existing {@link AysPage} object.
         *
         * @param page The source page object.
         * @return An instance of {@link AysPageResponseBuilder} with the page attributes set.
         */
        public <M> AysPageResponse.AysPageResponseBuilder<R> of(final AysPage<M> page) {
            return AysPageResponse.<R>builder()
                    .pageNumber(page.getPageNumber())
                    .pageSize(page.getPageSize())
                    .totalPageCount(page.getTotalPageCount())
                    .totalElementCount(page.getTotalElementCount())
                    .sortedBy(page.getSortedBy())
                    .filteredBy(page.getFilteredBy());
        }
    }
}
