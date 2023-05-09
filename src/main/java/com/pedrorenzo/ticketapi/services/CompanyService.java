package com.pedrorenzo.ticketapi.services;

import com.pedrorenzo.ticketapi.dtos.GetCompanyTicketsResponseDTO;

public interface CompanyService {
    GetCompanyTicketsResponseDTO findTicketsByCompanyId(Long id);
}
