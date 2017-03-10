package com.le.bigdata.core.datasource;


import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by benjamin on 16/5/23.
 */
public class MultiDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.get();
    }
}
