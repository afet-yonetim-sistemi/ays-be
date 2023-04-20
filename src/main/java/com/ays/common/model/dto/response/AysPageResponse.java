package com.ays.common.model.dto.response;

import com.ays.common.model.AysFiltering;
import com.ays.common.model.AysPage;
import com.ays.common.model.AysSorting;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AysPageResponse<R> {

    private List<R> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPageCount;
    private Long totalElementCount;
    private List<AysSorting> sortedBy;
    private AysFiltering filteredBy;


    public static class AysPageResponseBuilder<R> {
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
