package com.pedrorenzo.ticketapi.controllers;

import com.pedrorenzo.ticketapi.dtos.*;
import com.pedrorenzo.ticketapi.enums.TicketStatus;
import com.pedrorenzo.ticketapi.services.impl.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
public class TicketControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private TicketServiceImpl ticketService;

    protected MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .build();
    }

    @Test
    public void testFindTicketById() throws Exception {
        GetTicketResponseDTO responseDTO = new GetTicketResponseDTO();
        responseDTO.setCompanyId(5l);
        responseDTO.setId(1l);
        responseDTO.setDescription("Ticket desc");
        responseDTO.setStatus(TicketStatus.IN_QUEUE);
        when(ticketService.findById(1l, null)).thenReturn(responseDTO);

        this.mockMvc
                .perform(get("/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.companyId").value("5"))
                .andExpect(jsonPath("$.description").value("Ticket desc"))
                .andExpect(jsonPath("$.status").value("IN_QUEUE"))
                .andExpect(jsonPath("$.comments").doesNotExist());
    }

    @Test
    public void testFindTicketByIdWithComments() throws Exception {
        GetTicketResponseDTO responseDTO = new GetTicketResponseDTO();
        responseDTO.setCompanyId(5l);
        responseDTO.setId(1l);
        responseDTO.setDescription("Ticket desc");
        responseDTO.setStatus(TicketStatus.IN_QUEUE);

        GetTicketCommentResponseDTO commentResponseDTO1 = new GetTicketCommentResponseDTO();
        commentResponseDTO1.setCommentId(1l);
        commentResponseDTO1.setMessage("Message 1");
        commentResponseDTO1.setAuthorId(1l);

        GetTicketCommentResponseDTO commentResponseDTO2 = new GetTicketCommentResponseDTO();
        commentResponseDTO2.setCommentId(2l);
        commentResponseDTO2.setMessage("Message 2");
        commentResponseDTO2.setAuthorId(2l);

        responseDTO.setComments(Arrays.asList(commentResponseDTO1, commentResponseDTO2));
        when(ticketService.findById(1l, "comments")).thenReturn(responseDTO);

        this.mockMvc
                .perform(get("/tickets/1?include=comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.companyId").value("5"))
                .andExpect(jsonPath("$.description").value("Ticket desc"))
                .andExpect(jsonPath("$.status").value("IN_QUEUE"))
                .andExpect(jsonPath("$.comments[0].commentId").value("1"))
                .andExpect(jsonPath("$.comments[0].message").value("Message 1"))
                .andExpect(jsonPath("$.comments[0].authorId").value("1"))
                .andExpect(jsonPath("$.comments[1].commentId").value("2"))
                .andExpect(jsonPath("$.comments[1].message").value("Message 2"))
                .andExpect(jsonPath("$.comments[1].authorId").value("2"));
    }

    @Test
    public void testFindTicketByNonExistentId() throws Exception {
        when(ticketService.findById(999l, null)).thenThrow(new NoSuchElementException());

        this.mockMvc
                .perform(get("/tickets/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]").value("Resource not found"));
    }

    @Test
    public void testCreateTicket() throws Exception {
        CreateTicketRequestDTO createTicketRequestDTO = new CreateTicketRequestDTO();
        createTicketRequestDTO.setStatus(TicketStatus.IN_QUEUE);
        createTicketRequestDTO.setDescription("Desc");
        createTicketRequestDTO.setCompanyId(5l);

        CreateTicketResponseDTO createTicketResponseDTO = new CreateTicketResponseDTO();
        createTicketResponseDTO.setStatus(TicketStatus.IN_QUEUE);
        createTicketResponseDTO.setCompanyId(5l);
        createTicketResponseDTO.setDescription("Desc");
        createTicketResponseDTO.setId(1l);
        when(ticketService.create(createTicketRequestDTO)).thenReturn(createTicketResponseDTO);

        this.mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"companyId\": \"5\", \"description\": \"Desc\", \"status\": \"IN_QUEUE\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.companyId").value("5"))
                .andExpect(jsonPath("$.description").value("Desc"))
                .andExpect(jsonPath("$.status").value("IN_QUEUE"));
    }

    @Test
    public void testCreateTicketWithoutCompanyId() throws Exception {
        this.mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"Desc\", \"status\": \"IN_QUEUE\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("companyId: must not be null"));
    }

    @Test
    public void testCreateTicketWithoutDescription() throws Exception {
        this.mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"companyId\": \"5\", \"status\": \"IN_QUEUE\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("description: must not be empty"));
    }

    @Test
    public void testCreateTicketWithoutStatus() throws Exception {
        this.mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"companyId\": \"5\", \"description\": \"Desc\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("status: must not be null"));
    }

    @Test
    public void testAddCommentToTicket() throws Exception {
        AddCommentRequestDTO addCommentRequestDTO = new AddCommentRequestDTO();
        addCommentRequestDTO.setMessage("Cool message");
        addCommentRequestDTO.setAuthorId(5l);

        AddCommentResponseDTO addCommentResponseDTO = new AddCommentResponseDTO();
        addCommentResponseDTO.setTicketId(33l);
        addCommentResponseDTO.setId(1l);
        addCommentResponseDTO.setAuthorId(5l);
        addCommentResponseDTO.setMessage("Cool message");
        when(ticketService.addCommentToTicket(33l, addCommentRequestDTO)).thenReturn(addCommentResponseDTO);

        this.mockMvc.perform(post("/tickets/33/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\": \"Cool message\", \"authorId\": \"5\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.ticketId").value("33"))
                .andExpect(jsonPath("$.message").value("Cool message"))
                .andExpect(jsonPath("$.authorId").value("5"));
    }

    @Test
    public void testAddCommentToNonExistentTicket() throws Exception {
        AddCommentRequestDTO addCommentRequestDTO = new AddCommentRequestDTO();
        addCommentRequestDTO.setMessage("Cool message");
        addCommentRequestDTO.setAuthorId(5l);
        when(ticketService.addCommentToTicket(33l, addCommentRequestDTO)).thenThrow(new NoSuchElementException());

        this.mockMvc.perform(post("/tickets/33/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\": \"Cool message\", \"authorId\": \"5\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]").value("Resource not found"));
    }

    @Test
    public void testAddCommentToTicketWithoutAuthorId() throws Exception {
        this.mockMvc.perform(post("/tickets/33/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\": \"Cool message\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("authorId: must not be null"));
    }

    @Test
    public void testAddCommentToTicketWithoutMessage() throws Exception {
        this.mockMvc.perform(post("/tickets/33/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\": \"Cool message\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("authorId: must not be null"));
    }

    @Test
    public void testUpdateStatus() throws Exception {
        UpdateTicketStatusRequestDTO updateTicketStatusRequestDTO = new UpdateTicketStatusRequestDTO();
        AddCommentRequestDTO addCommentRequestDTO = new AddCommentRequestDTO();
        addCommentRequestDTO.setAuthorId(5l);
        addCommentRequestDTO.setMessage("Ticket resolved now, finally");
        updateTicketStatusRequestDTO.setComment(addCommentRequestDTO);
        updateTicketStatusRequestDTO.setStatus(TicketStatus.IN_QUEUE);
        doNothing().when(ticketService).updateStatus(1l, updateTicketStatusRequestDTO);

        this.mockMvc.perform(patch("/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"IN_QUEUE\"," +
                                "\"comment\": {\"message\": \"Ticket resolved now, finally\", \"authorId\": \"5\"}}"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateStatusWithoutComment() throws Exception {
        UpdateTicketStatusRequestDTO updateTicketStatusRequestDTO = new UpdateTicketStatusRequestDTO();
        updateTicketStatusRequestDTO.setStatus(TicketStatus.IN_QUEUE);
        doNothing().when(ticketService).updateStatus(1l, updateTicketStatusRequestDTO);

        this.mockMvc.perform(patch("/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"IN_QUEUE\"}"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateStatusWithoutStatus() throws Exception {
        UpdateTicketStatusRequestDTO updateTicketStatusRequestDTO = new UpdateTicketStatusRequestDTO();
        AddCommentRequestDTO addCommentRequestDTO = new AddCommentRequestDTO();
        addCommentRequestDTO.setAuthorId(5l);
        addCommentRequestDTO.setMessage("Ticket resolved now, finally");
        updateTicketStatusRequestDTO.setComment(addCommentRequestDTO);

        this.mockMvc.perform(patch("/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\": {\"message\": \"Ticket resolved now, finally\", \"authorId\": \"5\"}}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("status: must not be null"));;
    }

}