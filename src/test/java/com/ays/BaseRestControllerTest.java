package com.ays;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

//@WebMvcTest
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc(addFilters = false)
public abstract class BaseRestControllerTest {
}
