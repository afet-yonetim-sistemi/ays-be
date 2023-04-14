package com.ays.common.config.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
class AysHikariConfiguration {

    @Value("${ays.db.username}")
    private String username;

    @Value("${ays.db.password}")
    private String password;

    @Value("${ays.db.url}")
    private String url;

    @Value("${ays.db.maximum-pool-size}")
    private String maximumPoolSize;

    @Value("${ays.db.connection-timeout}")
    private String connectionTimeout;

    @Value("${ays.db.maximum-lifetime}")
    private String maximumLifetime;

    @Bean
    @ConditionalOnProperty(name = "ays.db.name", havingValue = "mysql")
    protected DataSource hikari() {
        log.debug("Hikari MySQL Datasource Configuration Call Started!");

        final AysHikariConfigMysqlProfile hikariConfigProfile = AysHikariConfigMysqlProfile.builder()
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
