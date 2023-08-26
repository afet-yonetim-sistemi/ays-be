package com.ays.assignment.util.exception;

import com.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception to be thrown when a user assignment with given latitude and longitude does not exist.
 */
public class AysAssignmentNotExistByPointException extends AysNotExistException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = 6807205838979849673L;

    /**
     * Constructs a new AysAssignmentNotExistByPointAndInstitutionIdException with the specified latitude and longitude.
     *
     * @param latitude  the latitude of location used for search
     * @param longitude the longitude of location used for search
     */
    public AysAssignmentNotExistByPointException(Double latitude, Double longitude) {
        super("ASSIGNMENT NOT EXIST! latitude:" + latitude.toString() + ", longitude:" + longitude.toString());
    }

}
