package com.pedrorenzo.ticketapi.controllers;

import com.pedrorenzo.ticketapi.dtos.GetCompanyTicketDetailsResponseDTO;
import com.pedrorenzo.ticketapi.dtos.GetCompanyTicketsResponseDTO;
import com.pedrorenzo.ticketapi.enums.TicketStatus;
import com.pedrorenzo.ticketapi.services.impl.CompanyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
public class CompanyControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private CompanyServiceImpl companyService;

    protected MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .build();
    }

    @Test
    public void testFindTicketsByCompanyId() throws Exception {
        GetCompanyTicketsResponseDTO responseDTO = new GetCompanyTicketsResponseDTO();
        GetCompanyTicketDetailsResponseDTO detailsDTO1 = new GetCompanyTicketDetailsResponseDTO();
        detailsDTO1.setTicketId(22l);
        detailsDTO1.setDescription("Description");
        detailsDTO1.setStatus(TicketStatus.IN_QUEUE);

        GetCompanyTicketDetailsResponseDTO detailsDTO2 = new GetCompanyTicketDetailsResponseDTO();
        detailsDTO2.setTicketId(33l);
        detailsDTO2.setDescription("Description 2");
        detailsDTO2.setStatus(TicketStatus.IN_PROGRESS);

        responseDTO.setTickets(Arrays.asList(detailsDTO1, detailsDTO2));
        responseDTO.setCompanyId(55l);
        when(companyService.findTicketsByCompanyId(55l)).thenReturn(responseDTO);

        this.mockMvc
                .perform(get("/companies/55/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyId").value("55"))
                .andExpect(jsonPath("$.tickets[0].ticketId").value("22"))
                .andExpect(jsonPath("$.tickets[0].description").value("Description"))
                .andExpect(jsonPath("$.tickets[0].status").value("IN_QUEUE"))
                .andExpect(jsonPath("$.tickets[1].ticketId").value("33"))
                .andExpect(jsonPath("$.tickets[1].description").value("Description 2"))
                .andExpect(jsonPath("$.tickets[1].status").value("IN_PROGRESS"));
    }

}