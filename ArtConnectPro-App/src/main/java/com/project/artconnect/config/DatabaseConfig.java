package com.project.artconnect.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConfig {
    private DatabaseConfig() {
    }

    public static final String URL = firstNonBlank(
            System.getProperty("artconnect.db.url"),
            System.getenv("ARTCONNECT_DB_URL"),
            "jdbc:mysql://localhost:3306/artconnect?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");

    public static final String USER = firstNonBlank(
            System.getProperty("artconnect.db.user"),
            System.getenv("ARTCONNECT_DB_USER"),
            "root");

    public static final String PASSWORD = firstNonBlank(
            System.getProperty("artconnect.db.password"),
            System.getenv("ARTCONNECT_DB_PASSWORD"),
            "!Ma87_Me04");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static String firstNonBlank(String first, String second, String fallback) {
        if (first != null && !first.isBlank()) {
            return first;
        }
        if (second != null && !second.isBlank()) {
            return second;
        }
        return fallback;
    }

}