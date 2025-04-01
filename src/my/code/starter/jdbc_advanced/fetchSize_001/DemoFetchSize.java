package my.code.starter.jdbc_advanced.fetchSize_001;

import my.code.starter.jdbc_core.util.ConnectionManager;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DemoFetchSize {
    public static void main(String[] args) throws SQLException {
        var result = getFlightsBetweenAndFetchSize(LocalDate.of(2020, 10, 1).atStartOfDay(),
                LocalDateTime.now());
        System.out.println(result);
    }

    private static List<Long> getFlightsBetweenAndFetchSize(LocalDateTime start, LocalDateTime end) throws SQLException {
        String sql = """
                SELECT id
                FROM flight
                WHERE departure_date BETWEEN ? AND ?
                """;
        List<Long> result = new ArrayList<>();
        try (var connection = ConnectionManager.open();
             var prepareStatement = connection.prepareStatement(sql)) {

            prepareStatement.setFetchSize(50); // Брати по n записів за раз
            prepareStatement.setQueryTimeout(10); // Чекати не більше sec секунд
            prepareStatement.setMaxRows(100); // Отримати не більше n записів

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
}
