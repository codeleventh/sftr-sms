package ru.eleventh.sftrsms.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.eleventh.sftrsms.common.ConfirmationState;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Store {
    @Value("${expirationTime}")
    private Integer expirationTime;

    @Value("${tries}")
    private Integer tries;

    private final ConcurrentHashMap<String, ConfirmationState> map = new ConcurrentHashMap<>();

    @Scheduled(fixedDelay = 60_000L)
    private void purgeOld() {
        for (val entry : map.entrySet()) {
            val info = entry.getValue();
            if (info.getCreatedTime().plusMinutes(expirationTime).isBefore(LocalDateTime.now()))
                map.remove(entry.getKey());
        }
    }

    public Boolean isExist(String phoneNumber) {
        return map.get(phoneNumber) == null;
    }

    public ConfirmationState get(String phoneNumber) {
        return map.get(phoneNumber);
    }

    public void add(String phoneNumber, String userId, String code) {
        val info = new ConfirmationState(code, userId, tries, LocalDateTime.now());
        map.put(phoneNumber, info);
    }

    public void remove(String phoneNumber) {
        map.remove(phoneNumber);
    }
}
