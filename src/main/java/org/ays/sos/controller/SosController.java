package org.ays.sos.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.common.model.response.AysResponse;
import org.ays.sos.model.request.SosRequest;
import org.ays.sos.service.SosService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller class for managing SOS emergency requests via HTTP requests.
 */
@Validated
@RestController
@RequiredArgsConstructor
class SosController {

    private final SosService sosService;

    /**
     * Create a new SOS emergency request.
     * This endpoint is accessible to authenticated mobile users only.
     *
     * @param sosRequest the SOS request containing the user's location
     * @return a response indicating the success of the operation
     */
    @PostMapping("/api/mobile/v1/sos")
    @PreAuthorize("isAuthenticated()")
    public AysResponse<Void> createSos(@RequestBody @Valid SosRequest sosRequest) {
        sosService.create(sosRequest);
        return AysResponse.success();
    }

}
