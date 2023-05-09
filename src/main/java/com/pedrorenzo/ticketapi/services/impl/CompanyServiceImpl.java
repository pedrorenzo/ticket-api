package com.pedrorenzo.ticketapi.services.impl;

import com.pedrorenzo.ticketapi.dtos.GetCompanyTicketsResponseDTO;
import com.pedrorenzo.ticketapi.entities.Ticket;
import com.pedrorenzo.ticketapi.mappers.TicketMapper;
import com.pedrorenzo.ticketapi.respositories.TicketRepository;
import com.pedrorenzo.ticketapi.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final TicketRepository ticketRepository;

    @Autowired
    public CompanyServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public GetCompanyTicketsResponseDTO findTicketsByCompanyId(Long id) {
        List<Ticket> tickets = ticketRepository.findByCompanyId(id);
        return TicketMapper.mapToGetCompanyTicketsResponseDTO(tickets, id);
    }

}
