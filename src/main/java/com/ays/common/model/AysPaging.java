package com.ays.common.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

/**
 * A class representing paging parameters for a query. It includes the page number and page size.
 * The page number is 1-based, but internally, it is stored as 0-based to comply with Spring Data's pagination system.
 */
@Getter
@Setter
public class AysPaging {

    /**
     * The page number, which should be greater than or equal to 1.
     */
    @NotNull
    @Range(min = 1)
    public Long page;

    /**
     * The number of elements in each page, which should be between 1 and 100.
     */
    @NotNull
    @Range(min = 10, max = 10)
    public Long pageSize;

    /**
     * Gets the 0-based page number, which is used internally by Spring Data's pagination system.
     *
     * @return The 0-based page number.
     */
    public Long getPage() {
        return this.page - 1;
    }

}
