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


/**
 * <p>
 * Provides the database configuration for the application, including the setup
 * of data sources, transaction managers, and entity managers.
 * </p>
 *
 * <p>
 * The configuration supports dynamic routing between read-write and read-only
 * data sources based on the current transaction context. Additionally, it
 * leverages HikariCP for connection pooling and integrates with JPA for
 * persistence.
 * </p>
 *
 * <p>This class is responsible for:</p>
 * <ul>
 *   <li>
 *     Configuring individual data sources for read-write and read-only
 *     operations.
 *   </li>
 *   <li>
 *     Creating a dynamically routed data source for handling transactions.
 *   </li>
 *   <li>
 *     Setting up JPA entity managers and transaction managers.
 *   </li>
 * </ul>
 */
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


    /**
     * <p>
     * Creates and configures a data source for read-only database operations.
     * </p>
     * <p>
     * The data source is configured with the provided username, password, URL,
     * and driver class name for accessing the read replica of the database.
     * </p>
     * @return a {@link DataSource} instance specifically designed for read-only operations.
     */
    @Bean
    DataSource readOnlyDataSource() {
        return DataSourceBuilder.create()
                .username(this.username)
                .password(this.password)
                .url(this.readerUrl)
                .driverClassName(this.driverClassName)
                .build();
    }

    /**
     * <p>
     * Creates and configures a {@link DataSource} for read-write database operations.
     * </p>
     *
     * <p>
     * The {@link DataSource} is configured with the provided username, password, URL,
     * and driver class name to allow write operations on the primary database.
     * </p>
     *
     * <p>
     * The {@code @LiquibaseDataSource} annotation designates this as the
     * {@link DataSource} to be used by Liquibase for database migrations.
     * </p>
     *
     * @return a {@link DataSource} instance configured for read-write operations.
     */
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

    /**
     * <p>
     * Creates and configures a {@link DataSource} using the HikariCP connection
     * pooling library.
     * </p>
     *
     * <p>
     * The data source is initialized with properties prefixed by
     * {@code spring.datasource.hikari} as defined in the application configuration.
     * </p>
     *
     * @return a configured {@link DataSource} instance built using HikariCP.
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    DataSource hikariDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    /**
     * <p>
     * Configures a routed {@link DataSource} that dynamically determines the target
     * data source based on the current transaction context
     * (read-only or read-write).
     * </p>
     *
     * @param readWriteDataSource
     *        the {@link DataSource} instance responsible for handling
     *        read-write operations
     * @param readOnlyDataSource
     *        the {@link DataSource} instance responsible for handling
     *        read-only operations
     * @return a {@link DataSource} implementation that dynamically routes
     *         database calls to either the read-write or read-only data source
     *         based on the current context
     */
    @Bean
    @Primary
    DataSource routingDataSource(DataSource readWriteDataSource, DataSource readOnlyDataSource) {
        return new AysTransactionRoutingDataSource(readWriteDataSource, readOnlyDataSource);
    }


    /**
     * <p>
     * Creates and configures a {@link LocalContainerEntityManagerFactoryBean}
     * for database operations with dynamic routing between read-write and
     * read-only data sources.
     * </p>
     *
     * <p>
     * The method uses a dynamically routed {@link DataSource}, determined based on
     * the current transaction context (read-write or read-only), and builds an
     * {@link EntityManagerFactory} using the specified packages.
     * </p>
     *
     * @param readWriteDataSource
     *        the {@link DataSource} responsible for handling
     *        read-write operations
     * @param readOnlyDataSource
     *        the {@link DataSource} responsible for handling
     *        read-only operations
     * @param entityManagerFactoryBuilder
     *        the {@link EntityManagerFactoryBuilder} used to configure and build
     *        the {@link LocalContainerEntityManagerFactoryBean}
     * @return a {@link LocalContainerEntityManagerFactoryBean} initialized with
     *         a routing {@link DataSource} and the specified configuration
     */
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

    /**
     * <p>
     * Configures and provides a custom {@link PlatformTransactionManager}
     * implementation.
     * </p>
     *
     * <p>
     * This transaction manager uses the {@link AysTransactionManager} to manage
     * transaction state and to route operations between read-only and read-write
     * data sources as required.
     * </p>
     *
     * @param jpaTransactionManager
     *        the underlying {@link PlatformTransactionManager} used to perform
     *        transaction operations
     * @return a {@link PlatformTransactionManager} instance that leverages
     *         the {@link AysTransactionManager} for dynamic transaction management
     */
    @Bean
    @Primary
    PlatformTransactionManager transactionManager(PlatformTransactionManager jpaTransactionManager) {
        return new AysTransactionManager(jpaTransactionManager);
    }

    /**
     * <p>
     * Configures and provides a {@link PlatformTransactionManager} for managing
     * JPA transactions.
     * </p>
     *
     * <p>
     * This method creates a {@link JpaTransactionManager} using the provided
     * {@link EntityManagerFactory}. The transaction manager is responsible for
     * handling transactional operations within JPA persistence contexts.
     * </p>
     *
     * @param entityManagerFactory
     *        the {@link EntityManagerFactory} used to create and manage
     *        JPA entity managers, facilitating interaction with the database
     * @return a configured {@link PlatformTransactionManager} instance for
     *         managing JPA transactions
     */
    @Bean
    PlatformTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
