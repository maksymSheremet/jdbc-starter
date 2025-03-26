package my.code.starter.util;

import my.code.starter.exception.ConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {
    private static final String URL = "jdbc:postgresql://localhost:5433/dmdev_jdbc_course";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    private ConnectionManager() {
    }

    static {
        loadDriver();
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ConnectionException("Failed to load PostgreSQL JDBC driver", e);
        }
    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new ConnectionException(e);
        }
    }
}
