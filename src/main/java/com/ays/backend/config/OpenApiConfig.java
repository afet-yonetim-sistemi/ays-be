package com.ays.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${application.title}")
    private String title;

    @Value("${application.licenseName}")
    private String licenseName;

    @Bean
    public OpenAPI customOpenAPI(@Value("${application.description}") String description,
                                 @Value("${application.version}") String version) {
        return new OpenAPI()
                .info(new Info().title("AYS")
                        .version(version)
                        .description(description)
                        .license(new License().name("AYS Licence")));
    }
}
