package my.code.starter.jdbc_advanced.batch_requests_004;

import my.code.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class DemoBatchRequests {
    public static void main(String[] args) throws SQLException {
        batchRequestsExample();
    }

    private static void batchRequestsExample() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = ConnectionManager.open();
            connection.setAutoCommit(false);

            statement = connection.createStatement();
            statement.addBatch("INSERT INTO airport (code, country, city) " +
                               "VALUES('SWT', 'United States', 'New York')");
            statement.addBatch("INSERT INTO airport (code, country, city) " +
                               "VALUES ('CIT', 'Ukraine', 'Kyiv')");

            int[] updateCounts = statement.executeBatch();
            System.out.println(Arrays.toString(updateCounts));

            connection.commit();

        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (statement != null) {
                statement.close();
            }

            if (connection != null) {
                connection.close();
            }
        }
    }
}
