package ru.eleventh.sftrsms.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ConfirmationState {

    public final String code, userId;

    public Integer tries;

    public final LocalDateTime createdTime;
}
