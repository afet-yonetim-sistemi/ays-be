package com.ays.assignment.model.dto.request;

import com.ays.common.model.AysPhoneNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AssignmentUpdateRequest {

    @NotBlank
    private String description;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Valid
    @NotNull
    private AysPhoneNumber phoneNumber;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

}
