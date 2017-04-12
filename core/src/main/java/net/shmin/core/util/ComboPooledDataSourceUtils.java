package net.shmin.core.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.util.Properties;

/**
 * C3P0工具类
 * Created by bjliuhongbin on 14-3-27.
 */
public class ComboPooledDataSourceUtils {
    public static void setProperties(ComboPooledDataSource dataSource, Properties properties) throws Exception {
        if (dataSource == null || properties == null) return;

        Integer acquireIncrement = (Integer) properties.get("acquireIncrement");
        if (acquireIncrement != null) {
            dataSource.setAcquireIncrement(acquireIncrement);
        }

        Integer acquireRetryAttempts = (Integer) properties.get("acquireRetryAttempts");
        if (acquireRetryAttempts != null) {
            dataSource.setAcquireRetryAttempts(acquireRetryAttempts);
        }

        Integer acquireRetryDelay = (Integer) properties.get("acquireRetryDelay");
        if (acquireRetryDelay != null) {
            dataSource.setAcquireRetryDelay(acquireRetryDelay);
        }

        Boolean autoCommitOnClose = (Boolean) properties.get("autoCommitOnClose");
        if (autoCommitOnClose != null) {
            dataSource.setAutoCommitOnClose(autoCommitOnClose);
        }

        String automaticTestTable = (String) properties.get("automaticTestTable");
        if (automaticTestTable != null) {
            dataSource.setAutomaticTestTable(automaticTestTable);
        }

        Boolean breakAfterAcquireFailure = (Boolean) properties.get("breakAfterAcquireFailure");
        if (breakAfterAcquireFailure != null) {
            dataSource.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
        }

        Integer checkoutTimeout = (Integer) properties.get("checkoutTimeout");
        if (checkoutTimeout != null) {
            dataSource.setCheckoutTimeout(checkoutTimeout);
        }

        String connectionTesterClassName = (String) properties.get("connectionTesterClassName");
        if (connectionTesterClassName != null) {
            dataSource.setConnectionTesterClassName(connectionTesterClassName);
        }

        String driverClass = (String) properties.get("driverClass");
        if (driverClass != null) {
            dataSource.setDriverClass(driverClass);
        }

        String factoryClassLocation = (String) properties.get("factoryClassLocation");
        if (factoryClassLocation != null) {
            dataSource.setFactoryClassLocation(factoryClassLocation);
        }

        Boolean forceIgnoreUnresolvedTransactions = (Boolean) properties.get("forceIgnoreUnresolvedTransactions");
        if (forceIgnoreUnresolvedTransactions != null) {
            dataSource.setForceIgnoreUnresolvedTransactions(forceIgnoreUnresolvedTransactions);
        }

        Integer idleConnectionTestPeriod = (Integer) properties.get("idleConnectionTestPeriod");
        if (idleConnectionTestPeriod != null) {
            dataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
        }

        Integer initialPoolSize = (Integer) properties.get("initialPoolSize");
        if (initialPoolSize != null) {
            dataSource.setInitialPoolSize(initialPoolSize);
        }

        String jdbcUrl = (String) properties.get("jdbcUrl");
        if (jdbcUrl != null) {
            dataSource.setJdbcUrl(jdbcUrl);
        }

        Integer maxIdleTime = (Integer) properties.get("maxIdleTime");
        if (maxIdleTime != null) {
            dataSource.setMaxIdleTime(maxIdleTime);
        }

        Integer maxPoolSize = (Integer) properties.get("maxPoolSize");
        if (maxPoolSize != null) {
            dataSource.setMaxPoolSize(maxPoolSize);
        }

        Integer maxStatements = (Integer) properties.get("maxStatements");
        if (maxStatements != null) {
            dataSource.setMaxStatements(maxStatements);
        }

        Integer maxStatementsPerConnection = (Integer) properties.get("maxStatementsPerConnection");
        if (maxStatementsPerConnection != null) {
            dataSource.setMaxStatementsPerConnection(maxStatementsPerConnection);
        }

        Integer minPoolSize = (Integer) properties.get("minPoolSize");
        if (minPoolSize != null) {
            dataSource.setMinPoolSize(minPoolSize);
        }

        Integer numHelperThreads = (Integer) properties.get("numHelperThreads");
        if (numHelperThreads != null) {
            dataSource.setNumHelperThreads(numHelperThreads);
        }

        String password = (String) properties.get("password");
        if (password != null) {
            dataSource.setPassword(password);
        }

        String preferredTestQuery = (String) properties.get("preferredTestQuery");
        if (preferredTestQuery != null) {
            dataSource.setPreferredTestQuery(preferredTestQuery);
        }

        Integer propertyCycle = (Integer) properties.get("propertyCycle");
        if (propertyCycle != null) {
            dataSource.setPropertyCycle(propertyCycle);
        }

        Boolean testConnectionOnCheckin = (Boolean) properties.get("testConnectionOnCheckin");
        if (testConnectionOnCheckin != null) {
            dataSource.setTestConnectionOnCheckin(testConnectionOnCheckin);
        }

        Boolean testConnectionOnCheckout = (Boolean) properties.get("testConnectionOnCheckout");
        if (testConnectionOnCheckout != null) {
            dataSource.setTestConnectionOnCheckout(testConnectionOnCheckout);
        }

        String user = (String) properties.get("user");
        if (user != null) {
            dataSource.setUser(user);
        }

    }

    public static boolean isEquals(ComboPooledDataSource dataSource, Properties properties) throws Exception {
        if (dataSource == null || properties == null) throw new RuntimeException("null");

        Integer acquireIncrement = (Integer) properties.get("acquireIncrement");
        if (acquireIncrement != null) {
            if (dataSource.getAcquireIncrement() != acquireIncrement) {
                return false;
            }
        }
        Integer acquireRetryAttempts = (Integer) properties.get("acquireRetryAttempts");
        if (acquireRetryAttempts != null) {
            if (dataSource.getAcquireRetryAttempts() != acquireRetryAttempts) {
                return false;
            }
        }

        Integer acquireRetryDelay = (Integer) properties.get("acquireRetryDelay");
        if (acquireRetryDelay != null) {
            if (dataSource.getAcquireRetryDelay() != acquireRetryDelay) {
                return false;
            }
        }

        Boolean autoCommitOnClose = (Boolean) properties.get("autoCommitOnClose");
        if (autoCommitOnClose != null) {
            if (dataSource.isAutoCommitOnClose() != autoCommitOnClose.booleanValue()) {
                return false;
            }
        }

        String automaticTestTable = (String) properties.get("automaticTestTable");
        if (automaticTestTable != null) {
            if (dataSource.getAutomaticTestTable() != automaticTestTable) {
                return false;
            }
        }

        Boolean breakAfterAcquireFailure = (Boolean) properties.get("breakAfterAcquireFailure");
        if (breakAfterAcquireFailure != null) {
            if (dataSource.isBreakAfterAcquireFailure() != breakAfterAcquireFailure.booleanValue()) {
                return false;
            }
        }

        Integer checkoutTimeout = (Integer) properties.get("checkoutTimeout");
        if (checkoutTimeout != null) {
            if (dataSource.getCheckoutTimeout() != checkoutTimeout) {
                return false;
            }
        }

        String connectionTesterClassName = (String) properties.get("connectionTesterClassName");
        if (connectionTesterClassName != null) {
            if (dataSource.getConnectionTesterClassName().equalsIgnoreCase(connectionTesterClassName)) {
                return false;
            }
        }

        String driverClass = (String) properties.get("driverClass");
        if (driverClass != null) {
            if (!dataSource.getDriverClass().equalsIgnoreCase(driverClass)) {
                return false;
            }
        }

        String factoryClassLocation = (String) properties.get("factoryClassLocation");
        if (factoryClassLocation != null) {
            if (!dataSource.getFactoryClassLocation().equalsIgnoreCase(factoryClassLocation)) {
                return false;
            }
        }

        Boolean forceIgnoreUnresolvedTransactions = (Boolean) properties.get("forceIgnoreUnresolvedTransactions");
        if (forceIgnoreUnresolvedTransactions != null) {
            if (dataSource.isForceIgnoreUnresolvedTransactions() != forceIgnoreUnresolvedTransactions.booleanValue()) {
                return false;
            }
        }

        Integer idleConnectionTestPeriod = (Integer) properties.get("idleConnectionTestPeriod");
        if (idleConnectionTestPeriod != null) {
            if (dataSource.getIdleConnectionTestPeriod() != idleConnectionTestPeriod) {
                return false;
            }
        }

        Integer initialPoolSize = (Integer) properties.get("initialPoolSize");
        if (initialPoolSize != null) {
            if (dataSource.getInitialPoolSize() != initialPoolSize) {
                return false;
            }
        }

        String jdbcUrl = (String) properties.get("jdbcUrl");
        if (jdbcUrl != null) {
            if (!dataSource.getJdbcUrl().equalsIgnoreCase(jdbcUrl)) {
                return false;
            }
        }

        Integer maxIdleTime = (Integer) properties.get("maxIdleTime");
        if (maxIdleTime != null) {
            if (dataSource.getMaxIdleTime() != maxIdleTime) {
                return false;
            }
        }

        Integer maxPoolSize = (Integer) properties.get("maxPoolSize");
        if (maxPoolSize != null) {
            if (dataSource.getMaxPoolSize() != maxPoolSize) {
                return false;
            }
        }

        Integer maxStatements = (Integer) properties.get("maxStatements");
        if (maxStatements != null) {
            if (dataSource.getMaxStatements() != maxStatements) {
                return false;
            }
        }

        Integer maxStatementsPerConnection = (Integer) properties.get("maxStatementsPerConnection");
        if (maxStatementsPerConnection != null) {
            if (dataSource.getMaxStatementsPerConnection() != maxStatementsPerConnection) {
                return false;
            }
        }

        Integer minPoolSize = (Integer) properties.get("minPoolSize");
        if (minPoolSize != null) {
            if (dataSource.getMinPoolSize() != minPoolSize) {
                return false;
            }
        }

        Integer numHelperThreads = (Integer) properties.get("numHelperThreads");
        if (numHelperThreads != null) {
            if (dataSource.getNumHelperThreads() != numHelperThreads) {
                return false;
            }
        }

        String password = (String) properties.get("password");
        if (password != null) {
            if (!dataSource.getPassword().equalsIgnoreCase(password)) {
                return false;
            }
        }

        String preferredTestQuery = (String) properties.get("preferredTestQuery");
        if (preferredTestQuery != null) {
            if (!dataSource.getPreferredTestQuery().equalsIgnoreCase(preferredTestQuery)) {
                return false;
            }
        }

        Integer propertyCycle = (Integer) properties.get("propertyCycle");
        if (propertyCycle != null) {
            if (dataSource.getPropertyCycle() != propertyCycle) {
                return false;
            }
        }

        Boolean testConnectionOnCheckin = (Boolean) properties.get("testConnectionOnCheckin");
        if (testConnectionOnCheckin != null) {
            if (dataSource.isTestConnectionOnCheckin() != testConnectionOnCheckin.booleanValue()) {
                return false;
            }
        }

        Boolean testConnectionOnCheckout = (Boolean) properties.get("testConnectionOnCheckout");
        if (testConnectionOnCheckout != null) {
            if (dataSource.isTestConnectionOnCheckout() != testConnectionOnCheckout.booleanValue()) {
                return false;
            }
        }

        String user = (String) properties.get("user");
        if (user != null) {
            if (!dataSource.getUser().equalsIgnoreCase(user)) {
                return false;
            }
        }

        return true;

    }
}
