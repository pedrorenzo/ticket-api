package com.pedrorenzo.ticketapi.mappers;

import com.pedrorenzo.ticketapi.dtos.AddCommentRequestDTO;
import com.pedrorenzo.ticketapi.dtos.AddCommentResponseDTO;
import com.pedrorenzo.ticketapi.entities.Comment;
import com.pedrorenzo.ticketapi.entities.Ticket;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment mapToComment(AddCommentRequestDTO from, Ticket ticket) {
        Comment to = new Comment();
        to.setTicket(ticket);
        to.setMessage(from.getMessage());
        to.setAuthorId(from.getAuthorId());
        to.setCreatedDate(LocalDateTime.now());
        return to;
    }

    public static AddCommentResponseDTO mapToAddCommentRequestDTO(Comment from) {
        AddCommentResponseDTO to = new AddCommentResponseDTO();
        if (from != null) {
            to.setAuthorId(from.getAuthorId());
            to.setMessage(from.getMessage());
            to.setId(from.getId());
            to.setTicketId(from.getTicket().getId());
        }
        return to;
    }

}
