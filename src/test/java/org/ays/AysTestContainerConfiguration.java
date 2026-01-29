package org.ays;

import com.redis.testcontainers.RedisContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

abstract class AysTestContainerConfiguration extends AysLogConfiguration {

    private static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.4.0")
            .withUsername("ays")
            .withPassword("ayspass")
            .withDatabaseName("test")
            .withCommand("--max-connections=1000");

    private static final RedisContainer REDIS_CONTAINER = new RedisContainer("redis:8.4.0");

    static {
        MYSQL_CONTAINER.start();
        REDIS_CONTAINER.start();
    }

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
        dynamicPropertyRegistry.add("spring.datasource.writer.url", MYSQL_CONTAINER::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.reader.url", MYSQL_CONTAINER::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        dynamicPropertyRegistry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));
    }

}
