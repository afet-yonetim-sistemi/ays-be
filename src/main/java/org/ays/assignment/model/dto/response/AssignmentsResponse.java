package org.ays.assignment.model.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.assignment.model.enums.AssignmentStatus;
import org.ays.common.model.dto.response.BaseResponse;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AssignmentsResponse extends BaseResponse {

    private String id;
    private AssignmentStatus status;
    private String description;
    private String firstName;
    private String lastName;
    private Location location;
    private User user;

    @Getter
    @Setter
    public static class Location {
        private Double longitude;
        private Double latitude;
    }

    @Getter
    @Setter
    public static class User {
        private String id;
        private String firstName;
        private String lastName;
    }
}
