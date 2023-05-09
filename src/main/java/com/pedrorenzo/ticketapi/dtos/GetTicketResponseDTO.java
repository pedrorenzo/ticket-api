package com.pedrorenzo.ticketapi.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pedrorenzo.ticketapi.enums.TicketStatus;

import java.util.List;

public class GetTicketResponseDTO {

    private Long id;
    private Long companyId;
    private String description;
    private TicketStatus status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<GetTicketCommentResponseDTO> comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public List<GetTicketCommentResponseDTO> getComments() {
        return comments;
    }

    public void setComments(List<GetTicketCommentResponseDTO> comments) {
        this.comments = comments;
    }

}
