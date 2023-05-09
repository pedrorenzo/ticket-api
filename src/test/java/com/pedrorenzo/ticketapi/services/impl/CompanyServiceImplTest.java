package com.pedrorenzo.ticketapi.services.impl;

import com.pedrorenzo.ticketapi.dtos.GetCompanyTicketsResponseDTO;
import com.pedrorenzo.ticketapi.entities.Comment;
import com.pedrorenzo.ticketapi.entities.Ticket;
import com.pedrorenzo.ticketapi.enums.TicketStatus;
import com.pedrorenzo.ticketapi.respositories.TicketRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceImplTest {

    @InjectMocks
    private CompanyServiceImpl companyService;

    @Mock
    private TicketRepository ticketRepository;

    @Test
    public void when_findTicketsByCompanyId_then_returnDTO() {
        // Ticket 1:
        Ticket ticket1 = new Ticket();
        ticket1.setStatus(TicketStatus.IN_PROGRESS);
        ticket1.setCompanyId(3l);
        ticket1.setId(1l);
        ticket1.setDescription("Ticket 1 desc.");
        ticket1.setCreatedDate(LocalDateTime.now().minusHours(1));
        ticket1.setLastUpdatedDate(LocalDateTime.now());
        Comment commentTicket1 = new Comment();
        commentTicket1.setTicket(ticket1);
        commentTicket1.setId(1l);
        commentTicket1.setMessage("Comment from ticket 1");
        commentTicket1.setAuthorId(1l);
        commentTicket1.setCreatedDate(LocalDateTime.now().minusHours(1));
        ticket1.setComments(Collections.singletonList(commentTicket1));

        // Ticket 2:
        Ticket ticket2 = new Ticket();
        ticket2.setStatus(TicketStatus.IN_QUEUE);
        ticket2.setCompanyId(3l);
        ticket2.setId(2l);
        ticket2.setDescription("Ticket 2 desc.");
        ticket2.setCreatedDate(LocalDateTime.now().minusHours(2));
        ticket2.setLastUpdatedDate(LocalDateTime.now().minusHours(1));
        ticket2.setComments(null);

        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket1);
        tickets.add(ticket2);
        when(ticketRepository.findByCompanyId(3l)).thenReturn(tickets);

        GetCompanyTicketsResponseDTO ticketsByCompanyId = companyService.findTicketsByCompanyId(3l);

        Assertions.assertEquals(3l, ticketsByCompanyId.getCompanyId());
        Assertions.assertEquals(2, ticketsByCompanyId.getTickets().size());

        // Validating ticket 1:
        Assertions.assertEquals(1l, ticketsByCompanyId.getTickets().get(0).getTicketId());
        Assertions.assertEquals(TicketStatus.IN_PROGRESS, ticketsByCompanyId.getTickets().get(0).getStatus());
        Assertions.assertEquals("Ticket 1 desc.", ticketsByCompanyId.getTickets().get(0).getDescription());

        // Validating ticket 2:
        Assertions.assertEquals(2l, ticketsByCompanyId.getTickets().get(1).getTicketId());
        Assertions.assertEquals(TicketStatus.IN_QUEUE, ticketsByCompanyId.getTickets().get(1).getStatus());
        Assertions.assertEquals("Ticket 2 desc.", ticketsByCompanyId.getTickets().get(1).getDescription());
    }

}
