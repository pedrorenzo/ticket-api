package com.pedrorenzo.ticketapi.services;

import com.pedrorenzo.ticketapi.dtos.*;

public interface TicketService {
    CreateTicketResponseDTO create(CreateTicketRequestDTO createTicketRequestDTO);
    GetTicketResponseDTO findById(Long id, String include);
    AddCommentResponseDTO addCommentToTicket(Long id, AddCommentRequestDTO addCommentRequestDTO);
    void updateStatus(Long id, UpdateTicketStatusRequestDTO updateTicketStatusRequestDTO);
}
