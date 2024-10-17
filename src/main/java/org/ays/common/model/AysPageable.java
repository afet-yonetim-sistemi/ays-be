package org.ays.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AysPageable extends AysSort {

    /**
     * Represents the page number.
     * This value should be between 1 and 99999999.
     */
    @Range(min = 1, max = 99999999)
    private int page;

    /**
     * Represents the size of the page.
     * The value must be 10 , specifying the number of items per page.
     */
    @Range(min = 10, max = 10, message = "must be 10")
    private int pageSize;

    /**
     * Converts the pagination parameters of this request to a Spring {@link Pageable} object.
     *
     * @return The {@link Pageable} object corresponding to this request's pagination parameters.
     */
    public Pageable toPageable() {

        if (CollectionUtils.isNotEmpty(this.getOrders())) {
            return PageRequest.of(
                    this.page - 1,
                    this.pageSize,
                    this.toSort()
            );
        }

        return PageRequest.of(
                this.page - 1,
                this.pageSize
        );
    }

}
