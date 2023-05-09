package com.pedrorenzo.ticketapi.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.ArrayList;
import java.util.List;

public class Response<T> {

    @JsonUnwrapped
    private T data;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> errors;

    public Response() {
    }

    public Response(final List<String> errors) {
        this.errors = errors;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getErrors() {
        if (this.errors == null) {
            this.errors = new ArrayList<String>();
        }
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

}
