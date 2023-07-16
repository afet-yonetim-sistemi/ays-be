package com.ays;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
abstract class AbstractTestContainerConfiguration {

    @Container
    static MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0.33");

    @DynamicPropertySource
    private static void overrideProps(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("ays.db.username", MYSQL_CONTAINER::getUsername);
        dynamicPropertyRegistry.add("ays.db.password", MYSQL_CONTAINER::getPassword);
        dynamicPropertyRegistry.add("ays.db.url", MYSQL_CONTAINER::getJdbcUrl);
    }

}
