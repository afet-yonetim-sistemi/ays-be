package com.ays.common.config.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * A configuration class that defines a Hikari DataSource bean for MySQL database. It reads the database connection properties
 * from the application properties file and creates a {@link HikariDataSource} object with the specified configuration.
 * The class uses {@link AysHikariMysqlConfigurationProfile} to construct the Hikari configuration object with the provided properties.
 * The bean is conditionally created based on the "ays.db.name" property value. It returns a HikariDataSource object when the
 * property value is "mysql".
 */
@Slf4j
@Configuration
class AysHikariConfiguration {

    /**
     * The database username.
     */
    @Value("${ays.db.username}")
    private String username;

    /**
     * The database password.
     */
    @Value("${ays.db.password}")
    private String password;

    /**
     * The database URL.
     */
    @Value("${ays.db.url}")
    private String url;

    /**
     * The maximum number of connections in the pool.
     */
    @Value("${ays.db.maximum-pool-size}")
    private String maximumPoolSize;

    /**
     * The maximum time in milliseconds that a connection is allowed to sit idle in the pool.
     */
    @Value("${ays.db.connection-timeout}")
    private String connectionTimeout;

    /**
     * The maximum lifetime of a connection in the pool.
     */
    @Value("${ays.db.maximum-lifetime}")
    private String maximumLifetime;

    /**
     * Creates a HikariDataSource bean for MySQL database with the specified configuration.
     * The bean is conditionally created based on the "ays.db.name" property value. It returns a HikariDataSource object when the
     * property value is "mysql".
     *
     * @return a HikariDataSource object
     */
    @Bean
    @ConditionalOnProperty(name = "ays.db.name", havingValue = "mysql")
    protected DataSource hikari() {
        log.debug("Hikari MySQL Datasource Configuration Call Started!");

        final AysHikariMysqlConfigurationProfile hikariConfigProfile = AysHikariMysqlConfigurationProfile.builder()
                .username(this.username)
                .password(this.password)
                .url(this.url)
                .maximumPoolSize(this.maximumPoolSize)
                .connectionTimeout(this.connectionTimeout)
                .maximumLifetime(this.maximumLifetime).build();

        final HikariConfig hikariConfig = hikariConfigProfile.toHikariConfig();

        log.debug("Hikari MySQL Datasource Successfully Configured!");
        return new HikariDataSource(hikariConfig);
    }
}
