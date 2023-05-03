package com.ays.common.model;

import org.junit.jupiter.api.Assertions;

public class AysPageBuilder extends TestDataBuilder<AysPage> {

    public AysPageBuilder() {
        super(AysPage.class);
    }

    public static <C> void assertEquals(AysPage<C> mockAysPage, AysPage<C> aysPage) {
        Assertions.assertEquals(mockAysPage.getContent().size(), aysPage.getContent().size());
        Assertions.assertEquals(mockAysPage.getPageNumber(), aysPage.getPageNumber());
        Assertions.assertEquals(mockAysPage.getPageSize(), aysPage.getPageSize());
        Assertions.assertEquals(mockAysPage.getTotalPageCount(), aysPage.getTotalPageCount());
        Assertions.assertEquals(mockAysPage.getSortedBy(), aysPage.getSortedBy());
        Assertions.assertEquals(mockAysPage.getFilteredBy(), aysPage.getFilteredBy());
    }

}
