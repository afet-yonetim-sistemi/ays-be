package org.ays.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for AysOpenAPI.
 * This class provides a custom OpenAPI configuration for the AYS software solutions.
 */
@Configuration
class AysOpenAPIConfiguration {

    @Value("${info.application.name}")
    private String title;

    @Value("${info.application.version}")
    private String version;

    /**
     * Creates a custom OpenAPI instance for AYS.
     *
     * @return The custom OpenAPI instance.
     */
    @Bean
    public OpenAPI openAPI() {

        final Contact contact = new Contact()
                .name(" with AYS Software Solutions")
                .email("iletisim@afetyonetimsistemi.org");

        final Info info = new Info()
                .title(this.title)
                .version(this.version)
                .description("Afet Yönetim Sistemi | AYS BE APIs Documentation")
                .contact(contact);

        return new OpenAPI().info(info);
    }

}
