package com.pedrorenzo.ticketapi.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AddCommentRequestDTO {

    @NotEmpty
    private String message;

    @NotNull
    private Long authorId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddCommentRequestDTO that = (AddCommentRequestDTO) o;

        if (!message.equals(that.message)) return false;
        return authorId.equals(that.authorId);
    }

    @Override
    public int hashCode() {
        int result = message.hashCode();
        result = 31 * result + authorId.hashCode();
        return result;
    }

}
