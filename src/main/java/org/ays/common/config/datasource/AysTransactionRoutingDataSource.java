package org.ays.common.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

class AysTransactionRoutingDataSource extends AbstractRoutingDataSource {

    private static DataSourceType currentDataSource = DataSourceType.READ_WRITE;

    public AysTransactionRoutingDataSource(DataSource readWriteDataSource, DataSource readOnlyDataSource) {

        super.setTargetDataSources(
                Map.of(
                        DataSourceType.READ_WRITE, readWriteDataSource,
                        DataSourceType.READ_ONLY, readOnlyDataSource
                )
        );

        super.setDefaultTargetDataSource(readWriteDataSource);
    }

    static void setReadonlyDataSource(boolean isReadonly) {

        if (isReadonly) {
            currentDataSource = DataSourceType.READ_ONLY;
            return;
        }

        currentDataSource = DataSourceType.READ_WRITE;
    }

    @Override
    public Object determineCurrentLookupKey() {
        return currentDataSource;
    }

    private enum DataSourceType {
        READ_ONLY,
        READ_WRITE
    }

}
