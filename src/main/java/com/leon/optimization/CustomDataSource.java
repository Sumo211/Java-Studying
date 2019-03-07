package com.leon.optimization;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

class CustomDataSource {

    private static HikariConfig config = new HikariConfig();

    private static HikariDataSource hikariDataSource;

    static {
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/transit_etl?prepareThreshold=0");
        config.setUsername("postgres");
        config.setPassword("Hoaibao211");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariDataSource = new HikariDataSource(config);
    }

    private CustomDataSource() {

    }

    static Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

}
