package org.ays.common.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * <p>
 * {@code AysTransactionRoutingDataSource} extends
 * {@link AbstractRoutingDataSource} to enable dynamic switching between
 * multiple data sources based on the current execution context.
 * </p>
 * <p>
 * It is particularly useful for managing scenarios with separate
 * read-only and read-write data sources within the same application.
 * </p>
 * <p>
 * This class maintains the current data source state using an internal
 * enum representation. The routing decision is tied to the transaction
 * context, allowing flexible and transparent delegation of database
 * operations to the appropriate data source.
 * </p>
 */
class AysTransactionRoutingDataSource extends AbstractRoutingDataSource {

    private static DataSourceType currentDataSource = DataSourceType.READ_WRITE;

    /**
     * <p>
     * Constructs a new {@code AysTransactionRoutingDataSource} instance by
     * configuring the target data sources and specifying the default
     * data source.
     * </p>
     *
     * @param readWriteDataSource
     *        the data source used for read-write operations;
     *        must not be {@code null}
     * @param readOnlyDataSource
     *        the data source used for read-only operations;
     *        must not be {@code null}
     */
    public AysTransactionRoutingDataSource(DataSource readWriteDataSource, DataSource readOnlyDataSource) {

        super.setTargetDataSources(
                Map.of(
                        DataSourceType.READ_WRITE, readWriteDataSource,
                        DataSourceType.READ_ONLY, readOnlyDataSource
                )
        );

        super.setDefaultTargetDataSource(readWriteDataSource);
    }

    /**
     * <p>
     * Configures the current data source to operate in either read-only or
     * read-write mode based on the provided parameter.
     * </p>
     *
     * @param isReadonly
     *        a boolean flag indicating whether the data source should be set
     *        to read-only mode ({@code true}) or read-write mode ({@code false})
     */
    static void setReadonlyDataSource(boolean isReadonly) {

        if (isReadonly) {
            currentDataSource = DataSourceType.READ_ONLY;
            return;
        }

        currentDataSource = DataSourceType.READ_WRITE;
    }

    /**
     * <p>
     * Determines the current lookup key used for routing to the appropriate
     * data source.
     * </p>
     *
     * <p>
     * This method dynamically resolves the target data source based on the
     * current execution context.
     * </p>
     *
     * @return the current lookup key representing the selected data source
     */
    @Override
    public Object determineCurrentLookupKey() {
        return currentDataSource;
    }

    private enum DataSourceType {
        READ_ONLY,
        READ_WRITE
    }

}
