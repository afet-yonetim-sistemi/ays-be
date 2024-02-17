package org.ays;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

abstract class AbstractTestContainerConfiguration {

    private static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0.33")
            .withUsername("ays")
            .withPassword("ayspass")
            .withDatabaseName("test");

    static {
        MYSQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
        dynamicPropertyRegistry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
    }

}
