package org.ays.auth.filter;

import org.ays.AysEndToEndTest;
import org.ays.auth.model.response.AysUserResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class AysBearerTokenAuthenticationFilterEndToEndTest extends AysEndToEndTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void givenValidRequest_whenRequestHasNotBearerToken_thenSkipAuthAndDoFilter() throws Exception {

        // Then
        String endpoint = "/public/actuator/info";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status()
                        .isOk());
    }

    @Test
    void givenValidRequest_whenRequestHasBearerToken_thenCheckTokenAndDoFilter() throws Exception {

        // Given
        String userId = AysValidTestData.SuperAdmin.ID;

        // Then
        String endpoint = "/api/institution/v1/user/".concat(userId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, superAdminToken.getAccessToken());

        AysResponse<AysUserResponse> mockResponse = AysResponse.successOf(new AysUserResponse());

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("id")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("emailAddress")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("firstName")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("lastName")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("phoneNumber.countryCode")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("phoneNumber.lineNumber")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("city")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("status")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("roles[*].id")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("roles[*].name")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("createdUser")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("createdAt")
                        .isNotEmpty());
    }

}
