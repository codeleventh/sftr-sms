package ru.eleventh.sftrsms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import ru.eleventh.sftrsms.error.ErrorMessage;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Data
public class Response {

    private final boolean success;

    @JsonInclude(NON_EMPTY)
    private final String error;

    public static Response of(ErrorMessage error) {
        return new Response(false, error.getMessage());
    }

    public static Response of(String errorMessage) {
        return new Response(false, errorMessage);
    }

    public static Response ok() {
        return new Response(true, null);
    }
}
