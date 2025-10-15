package org.ays.auth.config;

import lombok.RequiredArgsConstructor;
import org.ays.auth.filter.AysBearerTokenAuthenticationFilter;
import org.ays.auth.filter.AysRateLimitFilter;
import org.ays.auth.security.AysAuthenticationEntryPoint;
import org.ays.common.filter.AysAuditLogFilter;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security configuration class for the application.
 * <p>
 * This class configures and customizes web security using Spring Security. It sets up exception handling, CORS,
 * CSRF, session management, and custom filters to secure application endpoints. The configuration is designed
 * for stateless environments, making it ideal for REST APIs.
 * </p>
 * <p>
 * <strong>Key Components:</strong>
 * <ul>
 *   <li>
 *     <strong>Exception Handling:</strong> Configures a custom authentication entry point to handle unauthorized access.
 *   </li>
 *   <li>
 *     <strong>CORS Configuration:</strong> Sets up Cross-Origin Resource Sharing via a {@link CorsConfigurationSource}.
 *     When the "cors" profile is active, it uses restrictive settings based on external properties; otherwise, it applies
 *     a permissive configuration.
 *   </li>
 *   <li>
 *     <strong>CSRF Protection:</strong> Disabled to support stateless session management.
 *   </li>
 *   <li>
 *     <strong>Authorization Rules:</strong> Permits public access to specific endpoints (e.g., GET requests on "/public/**")
 *     and requires authentication for all other requests.
 *   </li>
 *   <li>
 *     <strong>Session Management:</strong> Configures stateless session management, ensuring that no HTTP sessions are maintained.
 *   </li>
 *   <li>
 *     <strong>Custom Filters:</strong> Integrates custom filters for audit logging, rate limiting, and bearer token authentication.
 *     These filters are added before the default {@link BearerTokenAuthenticationFilter} to ensure proper processing.
 *   </li>
 *   <li>
 *     <strong>Password Encoding:</strong> Declares a bean for password encoding using {@link BCryptPasswordEncoder}.
 *   </li>
 * </ul>
 * </p>
 * <p>
 * This centralized configuration ensures that all security aspects of the application are managed consistently and efficiently.
 * </p>
 *
 * @see HttpSecurity
 * @see SecurityFilterChain
 * @see CorsConfigurationSource
 * @see PasswordEncoder
 * @see AysAuditLogFilter
 * @see AysRateLimitFilter
 * @see AysBearerTokenAuthenticationFilter
 * @see AysAuthenticationEntryPoint
 */
@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
@EnableMethodSecurity
@RequiredArgsConstructor
class SecurityConfiguration {

    /**
     * Returns a new instance of the {@link RegisterSessionAuthenticationStrategy} class that
     * registers the session authentication strategy with the session registry.
     *
     * @return the new instance of {@link SessionAuthenticationStrategy}
     */
    @Bean
    SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    /**
     * Configures and builds the Spring Security filter chain.
     * <p>
     * This method sets up HTTP security by configuring:
     * <ul>
     *   <li>Exception handling with a custom authentication entry point.</li>
     *   <li>CORS settings using the provided {@link CorsConfigurationSource}.</li>
     *   <li>CSRF disablement.</li>
     *   <li>Authorization rules that permit public access to specific endpoints and require authentication for all other requests.</li>
     *   <li>Stateless session management.</li>
     *   <li>Custom filters for audit logging, rate limiting, and bearer token authentication, which are added before the default {@link BearerTokenAuthenticationFilter}.</li>
     * </ul>
     * </p>
     *
     * <p>
     * <strong>Security Note:</strong>
     * CSRF protection is deliberately disabled because the API is stateless and uses header-based JWT.
     * No session cookies are used.
     * Re-enable CSRF if cookie-based auth or browser forms are introduced.
     * <p>
     *
     * @param httpSecurity                    the {@link HttpSecurity} instance for configuring web-based security
     * @param auditLogFilter                  the custom filter for audit logging
     * @param rateLimitFilter                 the custom filter for rate limiting
     * @param bearerTokenAuthenticationFilter the custom filter for bearer token authentication
     * @param customAuthenticationEntryPoint  the entry point to handle authentication errors
     * @param corsConfigurationSource         the CORS configuration source
     * @return a fully configured {@link SecurityFilterChain} instance
     * @throws Exception if an error occurs during the configuration of the security filter chain
     */
    @Bean
    @SuppressWarnings("java:S4502")
    SecurityFilterChain filterChain(final HttpSecurity httpSecurity,
                                    final AysAuditLogFilter auditLogFilter,
                                    final AysRateLimitFilter rateLimitFilter,
                                    final AysBearerTokenAuthenticationFilter bearerTokenAuthenticationFilter,
                                    final AysAuthenticationEntryPoint customAuthenticationEntryPoint,
                                    final CorsConfigurationSource corsConfigurationSource)
            throws Exception {

        httpSecurity
                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(customAuthenticationEntryPoint))
                .cors(customizer -> customizer.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers(HttpMethod.GET, "/public/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/authentication/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/authentication/password/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/institution/v1/admin-registration-application/*/summary").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/institution/v1/admin-registration-application/*/complete").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/landing/v1/emergency-evacuation-application").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(auditLogFilter, BearerTokenAuthenticationFilter.class)
                .addFilterBefore(rateLimitFilter, BearerTokenAuthenticationFilter.class)
                .addFilterBefore(bearerTokenAuthenticationFilter, BearerTokenAuthenticationFilter.class);

        return httpSecurity.build();
    }

    /**
     * Configures and returns a {@link CorsConfigurationSource} bean when the "cors" profile is active.
     * <p>
     * This configuration uses properties defined in {@link CorsEndpointProperties} to set the allowed origins
     * and headers. The allowed HTTP methods are explicitly set to GET, POST, PUT, PATCH, DELETE, and OPTIONS.
     * Credentials are not allowed (set to false). The configuration is applied to all paths (/**).
     * </p>
     *
     * @param corsEndpointProperties the properties object containing CORS configuration values
     * @return a {@link CorsConfigurationSource} configured with the specified CORS settings
     */
    @Profile("cors")
    @Bean(name = "corsConfigurationSource")
    CorsConfigurationSource corsConfigurationActiveSource(final CorsEndpointProperties corsEndpointProperties) {

        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsEndpointProperties.getAllowedOrigins());
        final List<String> allowedMethods = List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
        );
        configuration.setAllowedMethods(allowedMethods);
        configuration.setAllowedHeaders(corsEndpointProperties.getAllowedHeaders());
        configuration.setAllowCredentials(false);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Configures and returns a {@link CorsConfigurationSource} bean when the "cors" profile is not active.
     * <p>
     * This configuration is permissive, allowing all origins, methods, and headers (each set to "*"),
     * with credentials disabled. The configuration is applied to all paths (/**).
     * </p>
     *
     * <p>
     * <strong>Security Note:</strong> This permissive CORS configuration is intended for local development environments.
     * It is not recommended for production use due to potential security risks associated with allowing unrestricted
     * cross-origin requests.
     * </p>
     *
     * @return a {@link CorsConfigurationSource} configured with permissive CORS settings
     */
    @Profile("!cors")
    @Bean(name = "corsConfigurationSource")
    @SuppressWarnings("java:S5122")
    CorsConfigurationSource corsConfigurationNotActiveSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Returns a new instance of the {@link BCryptPasswordEncoder} class that sets up the password encoder
     * for the application.
     *
     * @return the new instance of {@link PasswordEncoder}
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
