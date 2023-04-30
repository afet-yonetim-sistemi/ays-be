package com.ays.common.config.mysql;

import com.zaxxer.hikari.HikariConfig;
import lombok.Builder;

/**
 * This class represents a Hikari configuration for MySQL database. It contains the necessary parameters for configuring
 * a Hikari connection pool for a MySQL database. It provides a method to convert the configuration to a {@link HikariConfig}
 * object.
 */
@Builder
class AysHikariMysqlConfigurationProfile {

    /**
     * The username to use when connecting to the MySQL database.
     */
    private final String username;
    /**
     * The password to use when connecting to the MySQL database.
     */
    private final String password;
    /**
     * The URL of the MySQL database to connect to.
     */
    private final String url;
    /**
     * The maximum number of connections that can be allocated from this pool at the same time.
     */
    private final String maximumPoolSize;
    /**
     * The maximum time that a connection can remain idle in the pool before being removed and closed.
     */
    private final String connectionTimeout;
    /**
     * The maximum time to wait for a connection to be available in the pool.
     */
    private final String maximumLifetime;

    /**
     * Converts the configuration to a {@link HikariConfig} object that can be used to create a Hikari connection pool for
     * a MySQL database.
     *
     * @return a HikariConfig object representing the configuration.
     */
    public HikariConfig toHikariConfig() {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setUsername(this.username);
        hikariConfig.setPassword(this.password);
        hikariConfig.setJdbcUrl(this.url);
        hikariConfig.setMaximumPoolSize(Integer.parseInt(this.maximumPoolSize));
        hikariConfig.setConnectionTimeout(Long.parseLong(this.connectionTimeout));
        hikariConfig.setMaxLifetime(Long.parseLong(this.maximumLifetime));
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setAutoCommit(true);
        return hikariConfig;
    }
}
