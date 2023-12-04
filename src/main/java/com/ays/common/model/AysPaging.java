package com.ays.common.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
     * Represents the page number for pagination.
     * The page number must be greater than or equal to 1.
     * <p>
     * This field is annotated with {@code @NotNull} to indicate that it cannot be null.
     * Additionally, it is annotated with {@code @Min(1)} to specify that the value must be at least 1,
     * and {@code @Max(Integer.MAX_VALUE)} to set the maximum allowed value to Integer.MAX_VALUE.
     */
    @NotNull
    @Min(1)
    @Max(Integer.MAX_VALUE)
    public Long page;

    /**
     * Represents the number of elements to be displayed per page in a paginated result set.
     * The page size should be exactly 10.
     *
     * This field is annotated with {@code @NotNull} to indicate that it cannot be null.
     * Additionally, it is annotated with a custom or library-specific {@code @Range} annotation,
     * specifying that the valid page size is exactly 10.
     */
    @NotNull
    @Range(min = 10, max = 10)
    public Long pageSize;

    /**
     * Retrieves the 0-based page number, which is utilized internally by Spring Data's pagination system.
     * If the original page number is null, the method returns null.
     *
     * @return The 0-based page number or null if the original page number is null.
     */
    public Long getPage() {

        if (this.page == null) {
            return null;
        }

        return this.page - 1;
    }

}
