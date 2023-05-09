package com.pedrorenzo.ticketapi.dtos;

import com.pedrorenzo.ticketapi.enums.TicketStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;


@Validated
public class CreateTicketRequestDTO {

    @NotNull
    private Long companyId;

    @NotEmpty
    private String description;

    @NotNull
    private TicketStatus status;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreateTicketRequestDTO that = (CreateTicketRequestDTO) o;

        if (!companyId.equals(that.companyId)) return false;
        if (!description.equals(that.description)) return false;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        int result = companyId.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }

}
