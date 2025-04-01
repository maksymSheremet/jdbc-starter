package my.code.starter.data_access_object.dao;

import my.code.starter.data_access_object.entity.Ticket;
import my.code.starter.data_access_object.exception.DaoException;
import my.code.starter.jdbc_advanced.connection_pool_006.ConnectionPool;

import java.sql.SQLException;
import java.sql.Statement;

public class TicketDao {
    private static final String DELETE_TICKET_SQL = "DELETE FROM ticket  WHERE id = ?";
    private static final String INSERT_TICKET_SQL = """
            INSERT INTO ticket (passenger_no, passenger_name, flight_id, seat_no, cost)
            VALUES (?,?,?,?,?)
            """;

    private static final TicketDao INSTANCE = new TicketDao();

    private TicketDao() {
    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }

    public Ticket save(Ticket ticket) {
        try (var connection = ConnectionPool.getConnection();
             var prepareStatement = connection.prepareStatement(INSERT_TICKET_SQL, Statement.RETURN_GENERATED_KEYS)) {

            prepareStatement.setString(1, ticket.getPassengerNo());
            prepareStatement.setString(2, ticket.getPassengerName());
            prepareStatement.setLong(3, ticket.getFlightId());
            prepareStatement.setString(4, ticket.getSeatNo());
            prepareStatement.setBigDecimal(5, ticket.getCost());

            prepareStatement.executeUpdate();
            var generatedKeys = prepareStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                ticket.setId(generatedKeys.getLong("id"));
            }
            return ticket;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean delete(Long id) {
        try (var connection = ConnectionPool.getConnection();
             var prepareStatement = connection.prepareStatement(DELETE_TICKET_SQL)) {

            prepareStatement.setLong(1, id);
            return prepareStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DaoException("Failed to delete ticket with id " + id, e);
        }
    }
}
