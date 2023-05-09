package com.pedrorenzo.ticketapi.services.impl;

import com.pedrorenzo.ticketapi.dtos.*;
import com.pedrorenzo.ticketapi.entities.Comment;
import com.pedrorenzo.ticketapi.entities.Ticket;
import com.pedrorenzo.ticketapi.mappers.CommentMapper;
import com.pedrorenzo.ticketapi.mappers.TicketMapper;
import com.pedrorenzo.ticketapi.respositories.CommentRepository;
import com.pedrorenzo.ticketapi.respositories.TicketRepository;
import com.pedrorenzo.ticketapi.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    private final CommentRepository commentRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, CommentRepository commentRepository) {
        this.ticketRepository = ticketRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public CreateTicketResponseDTO create(CreateTicketRequestDTO createTicketRequestDTO) {
        Ticket ticket = TicketMapper.mapToTicket(createTicketRequestDTO);
        return TicketMapper.mapToCreateTicketResponseDTO(ticketRepository.save(ticket));
    }

    @Override
    public GetTicketResponseDTO findById(Long id, String include) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        return TicketMapper.mapToGetTicketResponseDTO(ticketOptional.orElseThrow(), isIncludeComments(include));
    }

    @Override
    public AddCommentResponseDTO addCommentToTicket(Long id, AddCommentRequestDTO addCommentRequestDTO) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        Comment comment = CommentMapper.mapToComment(addCommentRequestDTO, ticketOptional.orElseThrow());
        return CommentMapper.mapToAddCommentRequestDTO(commentRepository.save(comment));
    }

    @Override
    public void updateStatus(Long id, UpdateTicketStatusRequestDTO updateTicketStatusRequestDTO) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        Ticket ticket = ticketOptional.orElseThrow();
        ticket.setStatus(updateTicketStatusRequestDTO.getStatus());

        if (updateTicketStatusRequestDTO.getComment() != null) {
            addCommentToTicket(id, updateTicketStatusRequestDTO.getComment());
        }
        ticketRepository.save(ticket);
    }

    private boolean isIncludeComments(String include) {
        return "comments".equalsIgnoreCase(include);
    }

}
