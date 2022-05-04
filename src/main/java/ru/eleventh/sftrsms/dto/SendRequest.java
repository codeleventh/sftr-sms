package ru.eleventh.sftrsms.dto;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SendRequest {

    @Digits(integer = 10, fraction = 0)
    @Size(min = 10, max = 10)
    private final String phoneNumber;

    @NotBlank
    private final String userId;
}
