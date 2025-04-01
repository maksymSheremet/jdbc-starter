package my.code.starter.data_access_object.dao;

import my.code.starter.data_access_object.dto.TicketFilter;
import my.code.starter.data_access_object.entity.Flight;
import my.code.starter.data_access_object.entity.Ticket;
import my.code.starter.data_access_object.exception.DaoException;
import my.code.starter.jdbc_advanced.connection_pool_006.ConnectionPool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TicketDao {
    private static final String DELETE_TICKET_SQL = "DELETE FROM ticket  WHERE id = ?";
    private static final String INSERT_TICKET_SQL = """
            INSERT INTO ticket (passenger_no, passenger_name, flight_id, seat_no, cost)
            VALUES (?,?,?,?,?)
            """;
    private static final String UPDATE_TICKET_SQL = """
            UPDATE ticket
            SET passenger_no = ?, passenger_name = ?, flight_id = ?, seat_no = ?, cost = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_TICKETS_SQL = """
            SELECT ticket.id,
            passenger_no,
            passenger_name,
            flight_id,
            seat_no,
            cost,
            f.flight_no,
            f.status,
            f.aircraft_id,
            f.arrival_airport_code,
            f.arrival_date,
            f.departure_airport_code,
            f.departure_date
            FROM ticket
            JOIN flight f
            ON ticket.flight_id = f.id
            """;

    private static final String FIND_BY_ID_TICKET_SQL = FIND_ALL_TICKETS_SQL + """
            WHERE ticket.id = ?
            """;

    private static final TicketDao INSTANCE = new TicketDao();
    private static final FlightDao flightDao  = FlightDao.getInstance();

    private TicketDao() {
    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }

    public List<Ticket> findAll(TicketFilter filter) {
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();
        if (filter.seatNo() != null) {
            whereSql.add("seat_no LIKE ?");
            parameters.add("%" + filter.seatNo() + "%");
        }
        if (filter.passengerName() != null) {
            whereSql.add("passenger_name = ?");
            parameters.add(filter.passengerName());
        }
        parameters.add(filter.limit());
        parameters.add(filter.offset());
        var where = whereSql.stream()
                .collect(Collectors.joining(" AND ", " WHERE ", " LIMIT ? OFFSET ? "));
        var sql = FIND_ALL_TICKETS_SQL + (where.isEmpty() ? " " : where);
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            System.out.println(statement);
            var resultSet = statement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (resultSet.next()) {
                tickets.add(buildTicket(resultSet));
            }
            return tickets;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<Ticket> findAllTickets() {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(FIND_ALL_TICKETS_SQL)) {

            var resultSet = statement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (resultSet.next()) {
                tickets.add(buildTicket(resultSet));
            }
            return tickets;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<Ticket> getById(Long id) {
        try (var connection = ConnectionPool.getConnection();
             var prepareStatement = connection.prepareStatement(FIND_BY_ID_TICKET_SQL)) {

            prepareStatement.setLong(1, id);
            var resultSet = prepareStatement.executeQuery();

            Ticket ticket = null;
            if (resultSet.next()) {
                ticket = buildTicket(resultSet);
            }
            return Optional.ofNullable(ticket);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private static Ticket buildTicket(ResultSet resultSet) throws SQLException {
        var flight = new Flight(
                resultSet.getLong("flight_id"),
                resultSet.getString("flight_no"),
                resultSet.getTimestamp("departure_date").toLocalDateTime(),
                resultSet.getString("departure_airport_code"),
                resultSet.getTimestamp("arrival_date").toLocalDateTime(),
                resultSet.getString("arrival_airport_code"),
                resultSet.getInt("aircraft_id"),
                resultSet.getString("status")
        );
        return new Ticket(
                resultSet.getLong("id"),
                resultSet.getString("passenger_no"),
                resultSet.getString("passenger_name"),
//                flight,
//                flightDao.findById(resultSet.getLong("flight_id")).orElse(null),
                flightDao.findById(resultSet.getLong("flight_id"),
                        resultSet.getStatement().getConnection()).orElse(null),
                resultSet.getString("seat_no"),
                resultSet.getBigDecimal("cost")
        );
    }

    public Ticket update(Ticket ticket) {
        try (var connection = ConnectionPool.getConnection();
             var prepareStatement = connection.prepareStatement(UPDATE_TICKET_SQL)) {

            prepareStatement.setString(1, ticket.getPassengerNo());
            prepareStatement.setString(2, ticket.getPassengerName());
            prepareStatement.setLong(3, ticket.getFlight().id());
            prepareStatement.setString(4, ticket.getSeatNo());
            prepareStatement.setBigDecimal(5, ticket.getCost());
            prepareStatement.setLong(6, ticket.getId());

            prepareStatement.executeUpdate();

            return ticket;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Ticket save(Ticket ticket) {
        try (var connection = ConnectionPool.getConnection();
             var prepareStatement = connection.prepareStatement(INSERT_TICKET_SQL, Statement.RETURN_GENERATED_KEYS)) {

            prepareStatement.setString(1, ticket.getPassengerNo());
            prepareStatement.setString(2, ticket.getPassengerName());
            prepareStatement.setLong(3, ticket.getFlight().id());
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
