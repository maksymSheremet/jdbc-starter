package my.code.starter;

import my.code.starter.util.ConnectionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
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
