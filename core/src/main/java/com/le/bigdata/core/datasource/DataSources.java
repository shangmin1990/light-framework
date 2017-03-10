package com.le.bigdata.core.datasource;

/**
 * Created by benjamin on 16/5/23.
 */
public enum DataSources {

    DMP_DATA_STORE("dmp"),

    MATRIX_DATA_STORE("matrix"),

    MOBILE_LIVE("mLive");

    private String value;

    DataSources(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
