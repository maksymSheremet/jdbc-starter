package my.code.starter.data_access_object;

import my.code.starter.data_access_object.dao.TicketDao;
import my.code.starter.data_access_object.entity.Ticket;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class DaoRunner {
    public static void main(String[] args) {
        TicketDao ticketDao = TicketDao.getInstance();
        var allTickets = ticketDao.findAllTickets();
        System.out.println(allTickets);
    }

    private static void updateTicket() {
        TicketDao ticketDao = TicketDao.getInstance();
        var mayBeTicket = ticketDao.getById(2L);
        System.out.println(mayBeTicket);

        mayBeTicket.ifPresent(ticket -> {
            ticket.setCost(BigDecimal.valueOf(188.88));
            var updatedTicket = ticketDao.update(ticket);
            System.out.println(updatedTicket);
        });
    }

    private static void deleteTest() {
        TicketDao ticketDao = TicketDao.getInstance();
        var deleteResult = ticketDao.delete(56L);
        System.out.println(deleteResult);
    }

    private static void saveTest() {
        TicketDao ticketDao = TicketDao.getInstance();
        Ticket ticket = new Ticket();
        ticket.setPassengerNo("1234567");
        ticket.setPassengerName("Test");
        ticket.setFlightId(3L);
        ticket.setSeatNo("B3");
        ticket.setCost(BigDecimal.TEN);

        var savedTicket = ticketDao.save(ticket);
        System.out.println(savedTicket);
    }
}
