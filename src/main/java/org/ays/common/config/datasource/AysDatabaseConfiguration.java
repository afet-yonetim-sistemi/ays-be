package org.ays.common.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
class AysDatabaseConfiguration {

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
        return DataSourceBuilder.create()
                .username(this.username)
                .password(this.password)
                .url(this.readerUrl)
                .driverClassName(this.driverClassName)
                .build();
    }

    @Bean
    @LiquibaseDataSource
    DataSource readWriteDataSource() {
        return DataSourceBuilder.create()
                .username(this.username)
                .password(this.password)
                .url(this.writerUrl)
                .driverClassName(this.driverClassName)
                .build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    DataSource hikariDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    @Primary
    DataSource routingDataSource(DataSource readWriteDataSource, DataSource readOnlyDataSource) {
        return new AysTransactionRoutingDataSource(readWriteDataSource, readOnlyDataSource);
    }


    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource readWriteDataSource,
                                                                DataSource readOnlyDataSource,
                                                                EntityManagerFactoryBuilder entityManagerFactoryBuilder) {

        DataSource dataSource = routingDataSource(readWriteDataSource, readOnlyDataSource);
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("org.ays")
                .build();
    }

    @Bean
    @Primary
    PlatformTransactionManager transactionManager(PlatformTransactionManager jpaTransactionManager) {
        return new AysTransactionManager(jpaTransactionManager);
    }

    @Bean
    PlatformTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
