package ru.eleventh.sftrsms.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsService implements MessageService {

    private String formatPhoneNumber(String number) {
        return String.format("+7 %s %s %s", number.substring(0, 3), number.substring(3, 6), number.substring(6));
    }

    public void sendMessage(String code, String number) {
        log.info(String.format("Code '%s' was sent to %s", code, formatPhoneNumber(number)));
    }
}
