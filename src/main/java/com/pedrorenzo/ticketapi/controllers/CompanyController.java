package com.pedrorenzo.ticketapi.controllers;

import com.pedrorenzo.ticketapi.dtos.GetCompanyTicketsResponseDTO;
import com.pedrorenzo.ticketapi.response.Response;
import com.pedrorenzo.ticketapi.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * Gets the tickets from a company, by the company id.
     *
     * @param id The company id.
     * @return The ticket.
     */
    @GetMapping(value = "/{id}/tickets")
    public ResponseEntity<Response<GetCompanyTicketsResponseDTO>> findTicketsByCompanyId(@PathVariable("id") Long id) {
        Response<GetCompanyTicketsResponseDTO> response = new Response<>();
        response.setData(companyService.findTicketsByCompanyId(id));
        return ResponseEntity.ok(response);
    }

}
