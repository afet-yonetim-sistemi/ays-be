package com.ays.auth.config;

import com.ays.auth.filter.AysBearerTokenAuthenticationFilter;
import com.ays.auth.security.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
 * This class provides the security configuration for the application.
 * It is annotated with {@link Configuration}, {@link EnableWebSecurity} and {@link EnableGlobalAuthentication}.
 * The {@link SecurityFilterChain} is defined in the {@link #filterChain(HttpSecurity, AysBearerTokenAuthenticationFilter ,CustomAuthenticationEntryPoint )} (HttpSecurity, AysBearerTokenAuthenticationFilter)}
 * method which sets up the security configuration for HTTP requests.
 * The {@link SessionAuthenticationStrategy} is defined in the {@link #sessionAuthenticationStrategy()} method which registers
 * the session authentication strategy with the session registry.
 * The {@link PasswordEncoder} is defined in the {@link #passwordEncoder()} method which sets up the password encoder
 * for the application.
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
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    /**
     * Returns the {@link SecurityFilterChain} instance that defines the security configuration for HTTP requests.
     *
     * @param httpSecurity                    the {@link HttpSecurity} instance to configure
     * @param bearerTokenAuthenticationFilter the {@link AysBearerTokenAuthenticationFilter} instance to authenticate bearer tokens
     * @param customAuthenticationEntryPoint  the {@link CustomAuthenticationEntryPoint} instance to handle authentication errors
     * @return the {@link SecurityFilterChain} instance
     * @throws Exception if there is an error setting up the filter chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
                                           AysBearerTokenAuthenticationFilter bearerTokenAuthenticationFilter,
                                           CustomAuthenticationEntryPoint customAuthenticationEntryPoint)
            throws Exception {

        httpSecurity
                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(customAuthenticationEntryPoint))
                .cors(customizer -> customizer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers(HttpMethod.GET, "/public/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/public/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/authentication/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(bearerTokenAuthenticationFilter, BearerTokenAuthenticationFilter.class);

        return httpSecurity.build();
    }

    /**
     * Returns a new instance of the {@link UrlBasedCorsConfigurationSource} class that registers the
     * allowed origins, methods and headers for cross-origin resource sharing (CORS).
     *
     * @return the {@link CorsConfigurationSource} instance
     */
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
