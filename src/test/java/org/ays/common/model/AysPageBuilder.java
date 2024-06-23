package org.ays.common.model;

import org.junit.jupiter.api.Assertions;

import java.util.List;

public class AysPageBuilder {

    public static <C> AysPage<C> from(List<C> content, AysPageable aysPageable) {
        return AysPage.<C>builder()
                .content(content)
                .pageNumber(aysPageable.getPage())
                .pageSize(aysPageable.getPageSize())
                .totalPageCount(aysPageable.getPage())
                .totalElementCount((long) aysPageable.getPageSize())
                .orderedBy(aysPageable.getOrders())
                .build();
    }

    public static <C> AysPage<C> from(List<C> content, AysPageable aysPageable, AysFilter filter) {
        return AysPage.<C>builder()
                .content(content)
                .pageNumber(aysPageable.getPage())
                .pageSize(aysPageable.getPageSize())
                .totalPageCount(aysPageable.getPage())
                .totalElementCount((long) aysPageable.getPageSize())
                .orderedBy(aysPageable.getOrders())
                .filteredBy(filter)
                .build();
    }

    public static <C> void assertEquals(AysPage<C> mockAysPage, AysPage<C> aysPage) {
        Assertions.assertEquals(mockAysPage.getContent().size(), aysPage.getContent().size());
        Assertions.assertEquals(mockAysPage.getPageNumber(), aysPage.getPageNumber());
        Assertions.assertEquals(mockAysPage.getPageSize(), aysPage.getPageSize());
        Assertions.assertEquals(mockAysPage.getTotalPageCount(), aysPage.getTotalPageCount());
        Assertions.assertEquals(mockAysPage.getOrderedBy(), aysPage.getOrderedBy());
        Assertions.assertEquals(mockAysPage.getFilteredBy(), aysPage.getFilteredBy());
    }

}
