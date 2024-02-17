package org.ays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class for the Ays Application.
 * <p>This class is annotated with {@code @SpringBootApplication}, which combines three other annotations:
 * {@code @Configuration}, {@code @EnableAutoConfiguration}, and {@code @ComponentScan}.
 * <p>The {@code main} method is used to start the Spring Boot application by calling {@code SpringApplication.run}.
 *
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.boot.SpringApplication
 */
@SpringBootApplication
class AysApplication {
    public static void main(String[] args) {
        SpringApplication.run(AysApplication.class, args);
    }
}
