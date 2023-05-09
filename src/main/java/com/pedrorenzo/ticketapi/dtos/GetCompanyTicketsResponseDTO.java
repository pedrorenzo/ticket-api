package com.pedrorenzo.ticketapi.dtos;

import java.util.List;

public class GetCompanyTicketsResponseDTO {

    private Long companyId;
    private List<GetCompanyTicketDetailsResponseDTO> tickets;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public List<GetCompanyTicketDetailsResponseDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<GetCompanyTicketDetailsResponseDTO> tickets) {
        this.tickets = tickets;
    }

}
