package com.ays.common.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

/**
 * The class `Paging` is a data model used for pagination operations. It includes essential
 * pagination parameters such as page number and page size.
 * <p>
 * Instances of the `Paging` class can be used in pagination operations, and using this class
 * to validate pagination parameters from user input can be beneficial.
 * <p>
 * Note: This class is designed to assist in proper pagination operations and can be used
 * to ensure that parameters like page number or page size stay within certain bounds.
 */
@Getter
@Setter
public class AysPaging {

    /**
     * Represents the page number.
     * This value should be between 1 and 99999999.
     */
    @Range(min = 1, max = 99999999)
    public int page;

    /**
     * Represents the page size.
     * This value should be between 10 and 10, specifying the number of items per page.
     */
    @Range(min = 10, max = 10)
    public int pageSize;

}
