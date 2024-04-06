package org.ays;

import lombok.RequiredArgsConstructor;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.common.util.exception.model.AysErrorResponse;
import org.ays.util.AysMockResultMatchersBuilders;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@Component
@RequiredArgsConstructor
public class AysMockMvc {

    private final MockMvc mockMvc;

    public ResultActions perform(final MockHttpServletRequestBuilder mockHttpServletRequestBuilder,
                                 final AysResponse<?> mockResponse) throws Exception {

        return mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .isString())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockResponse.getHttpStatus().getReasonPhrase()))
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
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .isString())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockErrorResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .isBoolean())
                .andExpect(AysMockResultMatchersBuilders.header()
                        .isString())
                .andExpect(AysMockResultMatchersBuilders.header()
                        .value(mockErrorResponse.getHeader()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

}
