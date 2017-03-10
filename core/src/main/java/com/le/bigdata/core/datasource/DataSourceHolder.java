package com.le.bigdata.core.datasource;

/**
 * Created by benjamin on 16/5/23.
 */
public class DataSourceHolder {

    private DataSourceHolder() {

    }

    private static final ThreadLocal<DataSources> datasourceHolder = new ThreadLocal<DataSources>() {
        @Override
        protected DataSources initialValue() {
            return DataSources.MATRIX_DATA_STORE;
        }
    };

    public static DataSources get() {
        return datasourceHolder.get();
    }

    public static void set(DataSources dataSourceType) {
        datasourceHolder.set(dataSourceType);
    }

    public static void reset() {
        datasourceHolder.set(DataSources.MATRIX_DATA_STORE);
    }
}
