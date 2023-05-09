package com.pedrorenzo.ticketapi.exceptions;

import com.pedrorenzo.ticketapi.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
@RestController
public class ResponseExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Response> handleAllExceptions(final Exception ex) {
        return new ResponseEntity<>(new Response(Collections.singletonList(ex.getMessage())),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Response> handleMethodArgumentNotValidException(final MethodArgumentNotValidException
                                                                                        ex) {
        List<String> errorList = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());
        return new ResponseEntity<>(new Response(errorList), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public final ResponseEntity<Response> handleNoSuchElementException(final NoSuchElementException ex) {
        return new ResponseEntity<>(new Response(Collections.singletonList("Resource not found")),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<Response> handleHttpMessageNotReadableException(
            final HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(new Response(Collections.singletonList(ex.getLocalizedMessage())),
                HttpStatus.NOT_FOUND);
    }

}