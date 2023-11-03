package com.ays.common.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * A class representing paging parameters for a query. It includes the page number and page size.
 * The page number is 1-based, but internally, it is stored as 0-based to comply with Spring Data's pagination system.
 */
@Getter
@Setter
public class AysPaging {

    /**
     * The page number.
     *
     * <p>
     * The page number specifies the page of data to retrieve. It should be a positive integer greater than or equal to 1.
     * For example, setting it to 1 would request the first page of data.
     * </p>
     */
    @NotNull
    @Min(1)
    @Max(Integer.MAX_VALUE)
    public Long page;

    /**
     * The number of elements in each page.
     *
     * <p>
     * The pageSize field determines the number of elements to be included in each page of data. It should be a positive
     * integer within the range of 1 to the maximum value allowed (typically 1 to 100).
     * </p>
     */
    @NotNull
    @Min(1)
    @Max(Integer.MAX_VALUE)
    public Long pageSize;

    /**
     * Gets the 0-based page number, which is used internally by Spring Data's pagination system.
     *
     * @return The 0-based page number.
     */
    public Long getPage() {

        if (this.page == null) {
            return null;
        }

        return this.page - 1;
    }

}
