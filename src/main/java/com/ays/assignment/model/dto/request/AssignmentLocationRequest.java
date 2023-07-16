package com.ays.assignment.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignmentLocationRequest {

    private Double latitude;
    private Double longitude;
}
