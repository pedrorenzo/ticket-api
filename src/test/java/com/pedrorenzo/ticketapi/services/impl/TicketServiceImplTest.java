package com.pedrorenzo.ticketapi.services.impl;

import com.pedrorenzo.ticketapi.dtos.*;
import com.pedrorenzo.ticketapi.entities.Comment;
import com.pedrorenzo.ticketapi.entities.Ticket;
import com.pedrorenzo.ticketapi.enums.TicketStatus;
import com.pedrorenzo.ticketapi.respositories.CommentRepository;
import com.pedrorenzo.ticketapi.respositories.TicketRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private CommentRepository commentRepository;

    @Test
    public void when_create_then_returnDTO() {
        Ticket ticketSaved = new Ticket();
        ticketSaved.setCreatedDate(LocalDateTime.now());
        ticketSaved.setId(1l);
        ticketSaved.setDescription("Ticket to test");
        ticketSaved.setStatus(TicketStatus.IN_QUEUE);
        ticketSaved.setLastUpdatedDate(LocalDateTime.now());
        ticketSaved.setCompanyId(3l);

        ArgumentCaptor<Ticket> ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
        when(ticketRepository.save(ticketCaptor.capture())).thenReturn(ticketSaved);

        CreateTicketRequestDTO createTicketRequestDTO = new CreateTicketRequestDTO();
        createTicketRequestDTO.setCompanyId(3l);
        createTicketRequestDTO.setDescription("Ticket to test");
        createTicketRequestDTO.setStatus(TicketStatus.IN_QUEUE);

        CreateTicketResponseDTO createTicketResponseDTO = ticketService.create(createTicketRequestDTO);
        Assertions.assertEquals(ticketSaved.getId(), createTicketResponseDTO.getId());
        Assertions.assertEquals(ticketSaved.getStatus(), createTicketResponseDTO.getStatus());
        Assertions.assertEquals(ticketSaved.getDescription(), createTicketResponseDTO.getDescription());
        Assertions.assertEquals(ticketSaved.getCompanyId(), createTicketResponseDTO.getCompanyId());

        Assertions.assertEquals(createTicketRequestDTO.getCompanyId(), ticketCaptor.getValue().getCompanyId());
        Assertions.assertEquals(createTicketRequestDTO.getDescription(), ticketCaptor.getValue().getDescription());
        Assertions.assertEquals(createTicketRequestDTO.getStatus(), ticketCaptor.getValue().getStatus());
    }

    @Test
    public void when_findById_withExistentId_andWithComments_then_returnDTO() {
        Ticket ticket = new Ticket();
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setId(1l);
        ticket.setDescription("Ticket 1");
        ticket.setStatus(TicketStatus.RESOLVED);
        ticket.setLastUpdatedDate(LocalDateTime.now());
        ticket.setCompanyId(3l);
        Comment comment = new Comment();
        comment.setTicket(ticket);
        comment.setMessage("Message from comment 1, ticket 1");
        comment.setAuthorId(303l);
        comment.setId(1l);
        comment.setCreatedDate(LocalDateTime.now().minusHours(2));
        ticket.setComments(Collections.singletonList(comment));

        when(ticketRepository.findById(1l)).thenReturn(Optional.of(ticket));

        GetTicketResponseDTO actualDTO = ticketService.findById(1l, "comments");
        Assertions.assertEquals(ticket.getId(), actualDTO.getId());
        Assertions.assertEquals(ticket.getCompanyId(), actualDTO.getCompanyId());
        Assertions.assertEquals(ticket.getDescription(), actualDTO.getDescription());
        Assertions.assertEquals(ticket.getStatus(), actualDTO.getStatus());
        Assertions.assertEquals(ticket.getComments().size(), 1);
        Assertions.assertEquals(ticket.getComments().get(0).getId(),
                actualDTO.getComments().get(0).getCommentId());
        Assertions.assertEquals(ticket.getComments().get(0).getAuthorId(),
                actualDTO.getComments().get(0).getAuthorId());
        Assertions.assertEquals(ticket.getComments().get(0).getMessage(),
                actualDTO.getComments().get(0).getMessage());
    }

    @Test
    public void when_findById_withExistentId_andWithoutComments_then_returnDTO() {
        Ticket ticket = new Ticket();
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setId(1l);
        ticket.setDescription("Ticket 1");
        ticket.setStatus(TicketStatus.RESOLVED);
        ticket.setLastUpdatedDate(LocalDateTime.now());
        ticket.setCompanyId(3l);
        Comment comment = new Comment();
        comment.setTicket(ticket);
        comment.setMessage("Message from comment 1, ticket 1");
        comment.setAuthorId(303l);
        comment.setId(1l);
        comment.setCreatedDate(LocalDateTime.now().minusHours(2));
        ticket.setComments(Collections.singletonList(comment));

        when(ticketRepository.findById(1l)).thenReturn(Optional.of(ticket));

        GetTicketResponseDTO actualDTO = ticketService.findById(1l, null);
        Assertions.assertEquals(ticket.getId(), actualDTO.getId());
        Assertions.assertEquals(ticket.getCompanyId(), actualDTO.getCompanyId());
        Assertions.assertEquals(ticket.getDescription(), actualDTO.getDescription());
        Assertions.assertEquals(ticket.getStatus(), actualDTO.getStatus());
        Assertions.assertEquals(ticket.getComments().size(), 1);
        Assertions.assertNull(actualDTO.getComments());
    }

    @Test
    public void when_findById_withExistentId_andWithInvalidIncludeValue_then_returnDTO() {
        Ticket ticket = new Ticket();
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setId(1l);
        ticket.setDescription("Ticket 1");
        ticket.setStatus(TicketStatus.RESOLVED);
        ticket.setLastUpdatedDate(LocalDateTime.now());
        ticket.setCompanyId(3l);
        Comment comment = new Comment();
        comment.setTicket(ticket);
        comment.setMessage("Message from comment 1, ticket 1");
        comment.setAuthorId(303l);
        comment.setId(1l);
        comment.setCreatedDate(LocalDateTime.now().minusHours(2));
        ticket.setComments(Collections.singletonList(comment));

        when(ticketRepository.findById(1l)).thenReturn(Optional.of(ticket));

        GetTicketResponseDTO actualDTO = ticketService.findById(1l, "somethingInvalid");
        Assertions.assertEquals(ticket.getId(), actualDTO.getId());
        Assertions.assertEquals(ticket.getCompanyId(), actualDTO.getCompanyId());
        Assertions.assertEquals(ticket.getDescription(), actualDTO.getDescription());
        Assertions.assertEquals(ticket.getStatus(), actualDTO.getStatus());
        Assertions.assertEquals(ticket.getComments().size(), 1);
        Assertions.assertNull(actualDTO.getComments());
    }

    @Test
    public void when_findById_withNonExistentId_then_throwException() {
        when(ticketRepository.findById(1l)).thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> ticketService.findById(1l, null),
                "Expected findById() to throw NoSuchElementException"
        );
    }

    @Test
    public void when_addCommentToTicket_withExistentId_then_returnDTO() {
        // Mock find Ticket:
        Ticket ticket = new Ticket();
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setId(1l);
        ticket.setDescription("Ticket 1");
        ticket.setStatus(TicketStatus.RESOLVED);
        ticket.setLastUpdatedDate(LocalDateTime.now());
        ticket.setCompanyId(3l);
        when(ticketRepository.findById(1l)).thenReturn(Optional.of(ticket));

        // Mock save comment:
        Comment savedComment = new Comment();
        savedComment.setCreatedDate(LocalDateTime.now());
        savedComment.setMessage("Some cool message");
        savedComment.setTicket(ticket);
        savedComment.setAuthorId(98l);
        savedComment.setId(100l);
        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
        when(commentRepository.save(commentCaptor.capture())).thenReturn(savedComment);

        // Add the comment:
        AddCommentRequestDTO addCommentRequestDTO = new AddCommentRequestDTO();
        addCommentRequestDTO.setMessage("Some cool message");
        addCommentRequestDTO.setAuthorId(98l);
        AddCommentResponseDTO addCommentResponseDTO = ticketService.addCommentToTicket(1l, addCommentRequestDTO);

        Assertions.assertEquals(100l, addCommentResponseDTO.getId());
        Assertions.assertEquals(1l, addCommentResponseDTO.getTicketId());
        Assertions.assertEquals(addCommentRequestDTO.getAuthorId(), addCommentResponseDTO.getAuthorId());
        Assertions.assertEquals(addCommentRequestDTO.getMessage(), addCommentResponseDTO.getMessage());

        Assertions.assertEquals(addCommentRequestDTO.getMessage(), commentCaptor.getValue().getMessage());
        Assertions.assertEquals(1l, commentCaptor.getValue().getTicket().getId());
        Assertions.assertEquals(addCommentRequestDTO.getAuthorId(), commentCaptor.getValue().getAuthorId());
    }

    @Test
    public void when_addCommentToTicket_withNonExistentId_then_then_throwException() {
        when(ticketRepository.findById(1l)).thenReturn(Optional.empty());

        AddCommentRequestDTO addCommentRequestDTO = new AddCommentRequestDTO();
        addCommentRequestDTO.setMessage("Some cool message");
        addCommentRequestDTO.setAuthorId(98l);
        assertThrows(
                NoSuchElementException.class,
                () -> ticketService.addCommentToTicket(1l, addCommentRequestDTO),
                "Expected findById() to throw NoSuchElementException"
        );
    }

    @Test
    public void when_updateStatusWithComment_withExistentId_then_returnDTO() {
        // Mock find Ticket:
        Ticket ticket = new Ticket();
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setId(1l);
        ticket.setDescription("Ticket 1");
        ticket.setStatus(TicketStatus.RESOLVED);
        ticket.setLastUpdatedDate(LocalDateTime.now());
        ticket.setCompanyId(3l);
        when(ticketRepository.findById(1l)).thenReturn(Optional.of(ticket));

        // Mock save comment:
        Comment savedComment = new Comment();
        savedComment.setCreatedDate(LocalDateTime.now());
        savedComment.setMessage("Some cool message");
        savedComment.setTicket(ticket);
        savedComment.setAuthorId(98l);
        savedComment.setId(100l);
        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
        when(commentRepository.save(commentCaptor.capture())).thenReturn(savedComment);

        // Add the comment:
        UpdateTicketStatusRequestDTO updateTicketStatusRequestDTO = new UpdateTicketStatusRequestDTO();
        AddCommentRequestDTO addCommentRequestDTO = new AddCommentRequestDTO();
        addCommentRequestDTO.setAuthorId(98l);
        addCommentRequestDTO.setMessage("Some cool message");
        updateTicketStatusRequestDTO.setComment(addCommentRequestDTO);
        updateTicketStatusRequestDTO.setStatus(TicketStatus.RESOLVED);
        ticketService.updateStatus(1l, updateTicketStatusRequestDTO);

        Assertions.assertEquals(addCommentRequestDTO.getMessage(), commentCaptor.getValue().getMessage());
        Assertions.assertEquals(1l, commentCaptor.getValue().getTicket().getId());
        Assertions.assertEquals(addCommentRequestDTO.getAuthorId(), commentCaptor.getValue().getAuthorId());
    }

}
