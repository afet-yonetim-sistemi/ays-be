package com.ays.common.config.mysql;

import com.zaxxer.hikari.HikariConfig;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public class AysHikariConfigMysqlProfile {

    private final String username;
    private final String password;
    private final String url;
    private final String maximumPoolSize;
    private final String connectionTimeout;
    private final String maximumLifetime;

    public HikariConfig toHikariConfig() {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setUsername(this.username);
        hikariConfig.setPassword(this.password);
        hikariConfig.setJdbcUrl(this.url);
        log.info(this.url);
        log.info(this.username);
        log.info(this.password);
        hikariConfig.setMaximumPoolSize(Integer.parseInt(this.maximumPoolSize));
        hikariConfig.setConnectionTimeout(Long.parseLong(this.connectionTimeout));
        hikariConfig.setMaxLifetime(Long.parseLong(this.maximumLifetime));
        hikariConfig.setDriverClassName("software.aws.rds.jdbc.mysql.Driver");
        hikariConfig.setAutoCommit(true);
        return hikariConfig;
    }
}
