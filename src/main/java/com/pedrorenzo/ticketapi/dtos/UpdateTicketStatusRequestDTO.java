package com.pedrorenzo.ticketapi.dtos;

import com.pedrorenzo.ticketapi.enums.TicketStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class UpdateTicketStatusRequestDTO {

    @NotNull
    private TicketStatus status;

    @Valid
    private AddCommentRequestDTO comment;

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public AddCommentRequestDTO getComment() {
        return comment;
    }

    public void setComment(AddCommentRequestDTO comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdateTicketStatusRequestDTO that = (UpdateTicketStatusRequestDTO) o;

        if (status != that.status) return false;
        return Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }

}
