package org.ays;

import lombok.RequiredArgsConstructor;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.util.AysMockResultMatchersBuilders;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Component
@RequiredArgsConstructor
public class AysMockMvc {

    private final MockMvc mockMvc;

    public ResultActions perform(final MockHttpServletRequestBuilder mockHttpServletRequestBuilder) throws Exception {

        return mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    public ResultActions perform(final MockHttpServletRequestBuilder mockHttpServletRequestBuilder,
                                 final AysResponse<?> mockResponse) throws Exception {

        return mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .isBoolean())
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockResponse.getIsSuccess()));
    }

    public ResultActions perform(final MockHttpServletRequestBuilder mockHttpServletRequestBuilder,
                                 final AysErrorResponse mockErrorResponse) throws Exception {

        return mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.code()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .isBoolean())
                .andExpect(AysMockResultMatchersBuilders.header()
                        .isString())
                .andExpect(AysMockResultMatchersBuilders.header()
                        .value(mockErrorResponse.getHeader()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotHaveJsonPath());
    }

}
