package my.code.starter.jdbc_advanced.transactions_003;

import my.code.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionRunner {
    public static void main(String[] args) throws SQLException {
        Long flightId = 9L;
        var deleteFlightSql = "DELETE FROM flight WHERE id = ?";
        var deleteTicketsSql = "DELETE FROM ticket WHERE flight_id = ?";

        Connection connection = null;
        PreparedStatement deleteFlightStatement = null;
        PreparedStatement deleteTicketsStatement = null;
        try {
            connection = ConnectionManager.open();
            deleteFlightStatement = connection.prepareStatement(deleteFlightSql);
            deleteTicketsStatement = connection.prepareStatement(deleteTicketsSql);

            connection.setAutoCommit(false);

            deleteTicketsStatement.setLong(1, flightId);
            deleteFlightStatement.setLong(1, flightId);

            deleteTicketsStatement.executeUpdate();
            if (true) {
                throw new RuntimeException("Oops !!!");
            }
            deleteFlightStatement.executeUpdate();

            connection.commit();

        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (deleteFlightStatement != null) {
                deleteTicketsStatement.close();
            }

            if (deleteFlightStatement != null) {
                deleteFlightStatement.close();
            }

            if (connection != null) {
                connection.close();
            }
        }
    }
}
