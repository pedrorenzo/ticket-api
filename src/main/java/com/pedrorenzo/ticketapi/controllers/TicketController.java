package com.pedrorenzo.ticketapi.controllers;

import com.pedrorenzo.ticketapi.dtos.*;
import com.pedrorenzo.ticketapi.response.Response;
import com.pedrorenzo.ticketapi.services.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Validated
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Creates a ticket.
     *
     * @param createTicketRequestDTO The ticket to be created.
     * @return The created ticket.
     */
    @PostMapping
    public ResponseEntity<Response<CreateTicketResponseDTO>> create(@Valid @RequestBody CreateTicketRequestDTO
                                                                            createTicketRequestDTO) {
        CreateTicketResponseDTO createTicketResponseDTO = ticketService.create(createTicketRequestDTO);
        Response<CreateTicketResponseDTO> response = new Response<>();
        response.setData(createTicketResponseDTO);
        return ResponseEntity.created(URI.create("/tickets/" + createTicketResponseDTO.getId())).body(response);
    }

    /**
     * Gets a ticket by its id.
     *
     * @param id      The ticket id.
     * @param include Optional parameter, which allows the user to add additional info to this endpoint's response.
     * @return The ticket.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response<GetTicketResponseDTO>> findById(@PathVariable("id") Long id,
                                                                   @RequestParam(required = false, name = "include")
                                                                   String include) {
        Response<GetTicketResponseDTO> response = new Response<>();
        response.setData(ticketService.findById(id, include));
        return ResponseEntity.ok(response);
    }

    /**
     * Adds a comment to a ticket.
     *
     * @param addCommentRequestDTO The comment to be added to the ticket.
     * @return The created comment.
     */
    @PostMapping(value = "/{id}/comments")
    public ResponseEntity<Response<AddCommentResponseDTO>> addCommentToTicket(@PathVariable("id") Long id,
                                                                              @Valid @RequestBody AddCommentRequestDTO
                                                                                      addCommentRequestDTO) {
        AddCommentResponseDTO addCommentResponseDTO = ticketService.addCommentToTicket(id, addCommentRequestDTO);
        Response<AddCommentResponseDTO> response = new Response<>();
        response.setData(addCommentResponseDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Updates a ticket, setting the status and adding a new comment.
     *
     * @param id                           The ticket id.
     * @param updateTicketStatusRequestDTO The request to update the ticket.
     */
    @PatchMapping(value = "/{id}/status")
    public ResponseEntity<Void> updateStatus(@Valid @RequestBody UpdateTicketStatusRequestDTO
                                                         updateTicketStatusRequestDTO,
                                             @PathVariable("id") Long id) {
        ticketService.updateStatus(id, updateTicketStatusRequestDTO);
        return ResponseEntity.noContent().build();
    }

}
