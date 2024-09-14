package org.ays.common.config.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
class AysDataSourceConfiguration {

    @Value("${spring.datasource.reader.url}")
    private String readerUrl;

    @Value("${spring.datasource.writer.url}")
    private String writerUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Bean
    DataSource readOnlyDataSource() {
        HikariConfig config = new HikariConfig();
        config.setUsername(this.username);
        config.setPassword(this.password);
        config.setJdbcUrl(this.readerUrl);
        config.setDriverClassName(this.driverClassName);
        return new HikariDataSource(config);
    }

    @Bean
    DataSource readWriteDataSource() {
        HikariConfig config = new HikariConfig();
        config.setUsername(this.username);
        config.setPassword(this.password);
        config.setJdbcUrl(this.writerUrl);
        config.setDriverClassName(this.driverClassName);
        return new HikariDataSource(config);
    }

    @Bean
    @Primary
    @LiquibaseDataSource
    DataSource routingDataSource(DataSource readWriteDataSource, DataSource readOnlyDataSource) {
        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setTargetDataSources(
                Map.of(
                        DataSourceType.READ_WRITE, readWriteDataSource,
                        DataSourceType.READ_ONLY, readOnlyDataSource
                )
        );
        routingDataSource.setDefaultTargetDataSource(readWriteDataSource);
        return routingDataSource;
    }

    @Bean
    PlatformTransactionManager transactionManager(DataSource routingDataSource) {
        return new DataSourceTransactionManager(routingDataSource);
    }

    @Slf4j
    static class RoutingDataSource extends AbstractRoutingDataSource {

        @Override
        protected Object determineCurrentLookupKey() {

            boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

            if (isReadOnly) {
                log.warn("Routing to read-only data source");
                return DataSourceType.READ_ONLY;
            }

            log.warn("Routing to read-write data source");
            return DataSourceType.READ_WRITE;
        }

    }

    enum DataSourceType {
        READ_ONLY,
        READ_WRITE
    }

}
