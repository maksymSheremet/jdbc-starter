package my.code.starter;

import my.code.starter.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
//        Long flightId = 2L;
//        List<Long> result = getTicketsByFlightPS(flightId);
//        System.out.println(result);

        LocalDateTime start = LocalDate.of(2020, 10, 1).atStartOfDay();
        LocalDateTime end = LocalDateTime.now();
        List<Long> flights = getFlightsBetween(start, end);
        System.out.println(flights);
    }

    private static List<Long> getFlightsBetween(LocalDateTime start, LocalDateTime end) throws SQLException {
        String sql = """
                SELECT id
                FROM flight
                WHERE departure_date BETWEEN ? AND ?
                """;
        List<Long> result = new ArrayList<>();
        try (var connection = ConnectionManager.open();
             var prepareStatement = connection.prepareStatement(sql)) {

            System.out.println(prepareStatement);
            prepareStatement.setTimestamp(1, Timestamp.valueOf(start));
            System.out.println(prepareStatement);
            prepareStatement.setTimestamp(2, Timestamp.valueOf(end));
            System.out.println(prepareStatement);

            var resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getObject("id", Long.class));
            }
        }
        return result;
    }

    private static List<Long> getTicketsByFlightPS(Long flightId) throws SQLException {
        String sql = """
                SELECT id
                FROM ticket 
                WHERE flight_id = ?
                """;

        List<Long> result = new ArrayList<>();

        try (var connection = ConnectionManager.open();
             var prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setLong(1, flightId);
            var resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getObject("id", Long.class));
            }
        }
        return result;
    }

    private static void exampleInjection() throws SQLException {
        String flightId = "2 OR '' = ''";
//        String flightId = "2 OR 1 = 1; DROP TABLE info";
//        String flightId = "2 OR 1 = 1";
//        String flightId = "2";
        List<Long> result = getTicketsByFlight(flightId);
        System.out.println(result);
    }

    private static List<Long> getTicketsByFlight(String flightId) throws SQLException {
        String sql = """
                SELECT id
                FROM ticket 
                WHERE flight_id = %s
                """.formatted(flightId);

        List<Long> result = new ArrayList<>();

        try (var connection = ConnectionManager.open();
             var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
//                result.add(resultSet.getLong("id"));
                result.add(resultSet.getObject("id", Long.class)); // NULL safe
            }
        }
        return result;
    }

}
