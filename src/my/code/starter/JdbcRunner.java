package my.code.starter;

import my.code.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
        Class<Driver> driverClass = Driver.class;
        String createDatabase = """
                CREATE DATABASE my_database;
                """;
        String dropDatabase = "DROP DATABASE IF EXISTS my_database";
        String createSchema = "CREATE SCHEMA IF NOT EXISTS my_schema";

        String sql = """
                CREATE TABLE IF NOT EXISTS info (
                    id SERIAL PRIMARY KEY,
                    data TEXT NOT NULL
                );
                """;
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {
            System.out.println(connection.getSchema());
            System.out.println(connection.getTransactionIsolation());
            var executeResult = statement.execute(sql);
            System.out.println(executeResult);
        }
    }
}
