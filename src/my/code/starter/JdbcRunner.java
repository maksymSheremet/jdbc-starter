package my.code.starter;

import my.code.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
        Class<Driver> driverClass = Driver.class;

    }

    private static void exampleResultSet() throws SQLException {
        String sql = """
                SELECT *
                FROM ticket
                """;
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {
            //Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)
            System.out.println(connection.getSchema());
            System.out.println(connection.getTransactionIsolation());

            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                System.out.println(resultSet.getLong("id"));
                System.out.println(resultSet.getString("passenger_no"));
                System.out.println(resultSet.getBigDecimal("cost"));
//                resultSet.updateLong("id", 100);
//                resultSet.beforeFirst();
                System.out.println("--------------------------------");
            }
        }
    }

    private static void exampleDML() throws SQLException {
        //        String sql_insert = """
//                INSERT INTO  info(data)
//                VALUES ('Test1'),
//                        ('Test2'),
//                        ('Test3'),
//                        ('Test4');
//
//                INSERT INTO  info(data)
//                VALUES ('Test1'),
//                        ('Test2'),
//                        ('Test3'),
//                        ('Test4');
//                """;
        String sql_insert = """
                INSERT INTO  info(data)
                VALUES ('Test1'),
                        ('Test2'),
                        ('Test3'),
                        ('Test4');
                """;

//        String sql_update = """
//                UPDATE info
//                SET data = 'Updated Test'
//                WHERE id = 5;
//                """;

        String sql_update_return = """
                UPDATE info
                SET data = 'Updated Test'
                WHERE id = 5
                RETURNING *;
                """;

        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {
            System.out.println(connection.getSchema());
            System.out.println(connection.getTransactionIsolation());

//            boolean executeResult = statement.execute(sql_insert);
//            int executeResult = statement.executeUpdate(sql_update);
            boolean executeResult = statement.execute(sql_update_return);
            System.out.println(executeResult);
            System.out.println(statement.getUpdateCount());
        }
    }

    private static void exampleDDL() throws SQLException {
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
