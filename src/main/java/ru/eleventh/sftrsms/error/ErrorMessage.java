package ru.eleventh.sftrsms.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum ErrorMessage {

    DEFAULT("Something goes wrong"),
    NOT_AUTHORIZED("Not authorized"),
    WRONG_FORMAT("Request data is invalid"),
    WRONG_USER("The user id is wrong"),
    WRONG_CODE("The code is wrong"),
    ALREADY_HAVE("The code for the phone number was already send"),
    NO_NUMBER_STORED("The confirmation was not expected for this phone number");

    final String message;
}
