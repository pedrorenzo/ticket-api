package com.pedrorenzo.ticketapi.dtos;

import com.pedrorenzo.ticketapi.enums.TicketStatus;

public class GetCompanyTicketDetailsResponseDTO {

    private Long ticketId;
    private String description;
    private TicketStatus status;

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

}
