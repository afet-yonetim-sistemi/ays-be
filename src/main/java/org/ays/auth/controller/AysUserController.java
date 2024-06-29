package org.ays.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.mapper.AysUserToAysUserListResponseMapper;
import org.ays.auth.model.request.AysUserListRequest;
import org.ays.auth.model.response.AysUserListResponse;
import org.ays.auth.service.AysUserReadService;
import org.ays.common.model.AysPage;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
class AysUserController {


    private final AysUserReadService aysUserReadService;

    private final AysUserToAysUserListResponseMapper aysUserListResponseMapper = AysUserToAysUserListResponseMapper.initialize();

    //todo javadoc and cover with test

    @PostMapping("/users")
    @PreAuthorize("hasAnyAuthority('user:list')")
    public AysResponse<AysPageResponse<AysUserListResponse>> findAllByInstitutionId(@RequestBody @Valid AysUserListRequest request) {

        AysPage<AysUser> pageOfAysUsers = aysUserReadService.findAllByInstitutionId(request);

        final AysPageResponse<AysUserListResponse> pageOfUsersResponse = AysPageResponse.<AysUserListResponse>builder()
                .of(pageOfAysUsers)
                .content(aysUserListResponseMapper.map(pageOfAysUsers.getContent()))
                .build();

        return AysResponse.successOf(pageOfUsersResponse);
    }

}