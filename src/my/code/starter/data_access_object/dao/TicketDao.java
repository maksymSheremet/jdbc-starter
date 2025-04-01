package my.code.starter.data_access_object.dao;

public class TicketDao {

    public static final TicketDao INSTANCE = new TicketDao();

    private TicketDao() {
    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }
}
