package com.ays.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for customizing OpenAPI documentation.
 */
@Configuration
class OpenApiConfig {

    /**
     * Application title property value.
     */
    @Value("${application.title}")
    private String title;

    /**
     * Application license name property value.
     */
    @Value("${application.licenseName}")
    private String licenseName;

    /**
     * Creates a custom OpenAPI object with the provided application description and version.
     *
     * @param description the application description property value.
     * @param version     the application version property value.
     * @return a custom OpenAPI object with the specified properties.
     */
    @Bean
    public OpenAPI customOpenAPI(@Value("${application.description}") String description,
                                 @Value("${application.version}") String version) {
        return new OpenAPI()
                .info(new Info().title(title)
                        .version(version)
                        .description(description)
                        .license(new License().name(licenseName)));
    }
}
