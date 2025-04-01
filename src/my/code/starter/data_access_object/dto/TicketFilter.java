package my.code.starter.data_access_object.dto;

public record TicketFilter(int limit,
                           int offset,
                           String seatNo,
                           String passengerName) {
}
