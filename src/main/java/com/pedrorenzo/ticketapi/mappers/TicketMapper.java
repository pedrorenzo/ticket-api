package com.pedrorenzo.ticketapi.mappers;

import com.pedrorenzo.ticketapi.dtos.*;
import com.pedrorenzo.ticketapi.entities.Comment;
import com.pedrorenzo.ticketapi.entities.Ticket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketMapper {

    public static Ticket mapToTicket(CreateTicketRequestDTO from) {
        Ticket to = new Ticket();
        to.setCompanyId(from.getCompanyId());
        to.setDescription(from.getDescription());
        to.setStatus(from.getStatus());
        to.setCreatedDate(LocalDateTime.now());
        to.setLastUpdatedDate(LocalDateTime.now());
        return to;
    }

    public static CreateTicketResponseDTO mapToCreateTicketResponseDTO(Ticket from) {
        CreateTicketResponseDTO to = new CreateTicketResponseDTO();
        if (from != null) {
            to.setCompanyId(from.getCompanyId());
            to.setDescription(from.getDescription());
            to.setId(from.getId());
            to.setStatus(from.getStatus());
        }
        return to;
    }

    public static GetCompanyTicketsResponseDTO mapToGetCompanyTicketsResponseDTO(List<Ticket> tickets, Long companyId) {
        List<GetCompanyTicketDetailsResponseDTO> detailsResponseDTOs = new ArrayList<>();
        GetCompanyTicketDetailsResponseDTO detailsResponseDTO;
        for (Ticket ticket : tickets) {
            detailsResponseDTO = new GetCompanyTicketDetailsResponseDTO();
            detailsResponseDTO.setTicketId(ticket.getId());
            detailsResponseDTO.setDescription(ticket.getDescription());
            detailsResponseDTO.setStatus(ticket.getStatus());
            detailsResponseDTOs.add(detailsResponseDTO);
        }

        GetCompanyTicketsResponseDTO to = new GetCompanyTicketsResponseDTO();
        to.setCompanyId(companyId);
        to.setTickets(detailsResponseDTOs);
        return to;
    }

    public static List<GetTicketCommentResponseDTO> mapToGetTicketCommentResponseDTOs(List<Comment> from) {
        List<GetTicketCommentResponseDTO> to = new ArrayList<>();
        GetTicketCommentResponseDTO commentResponseDTO;
        for (Comment comment : from) {
            commentResponseDTO = new GetTicketCommentResponseDTO();
            commentResponseDTO.setAuthorId(comment.getAuthorId());
            commentResponseDTO.setMessage(comment.getMessage());
            commentResponseDTO.setCommentId(comment.getId());
            to.add(commentResponseDTO);
        }
        return to;
    }

    public static GetTicketResponseDTO mapToGetTicketResponseDTO(Ticket from, boolean includeComment) {
        GetTicketResponseDTO to = new GetTicketResponseDTO();
        if (includeComment) {
            to.setComments(mapToGetTicketCommentResponseDTOs(from.getComments()));
        }
        to.setCompanyId(from.getCompanyId());
        to.setId(from.getId());
        to.setStatus(from.getStatus());
        to.setDescription(from.getDescription());
        return to;
    }

}
