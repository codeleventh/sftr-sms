package ru.eleventh.sftrsms.service;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.eleventh.sftrsms.common.CodeGenerator;
import ru.eleventh.sftrsms.common.ConfirmationState;
import ru.eleventh.sftrsms.error.BusinessException;
import ru.eleventh.sftrsms.error.ErrorMessage;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class CodeServiceTest {


    String number = "999123456", userId = "durov", code = "00000";

    @Mock
    Store store;

    @Mock
    CodeGenerator generator;

    @Mock
    MessageService messageService;

    @InjectMocks
    CodeService codeService;

    @Test
    void sendCode() {
        when(generator.next()).thenReturn(code);

        codeService.sendCode(number, userId);

        verify(generator).next();
        verify(messageService).sendMessage(code, number);
        verify(store).add(number, userId, code);
    }

    @Test
    void sendCode_alreadySend() {
        when(store.get(number)).thenReturn(new ConfirmationState(number, userId, 1, LocalDateTime.now()));

        val exception = assertThrows(BusinessException.class, () -> codeService.sendCode(number, userId));
        assertEquals(ErrorMessage.ALREADY_HAVE.getMessage(), exception.getMessage());
    }

    @Test
    void verifyCode() {
        val confirmationState = new ConfirmationState(code, userId, 1, LocalDateTime.now());
        when(store.get(number)).thenReturn(confirmationState);

        assertDoesNotThrow(() -> codeService.verifyCode(code, number, userId));
        verify(store).remove(number);
    }

    @Test
    void verifyCode_wrongCode() {
        val confirmationState = new ConfirmationState(code, userId, Integer.MAX_VALUE, LocalDateTime.now());
        when(store.get(number)).thenReturn(confirmationState);

        val wrongCode = "wrong code";
        val exception = assertThrows(BusinessException.class, () -> codeService.verifyCode(wrongCode, number, userId));
        assertEquals(ErrorMessage.WRONG_CODE.getMessage(), exception.getMessage());
    }

    @Test
    void verifyCode_triesAreReduced() {
        val tries = 1;
        val confirmationState = new ConfirmationState(code, userId, tries, LocalDateTime.now());
        when(store.get(number)).thenReturn(confirmationState);

        val wrongCode = "wrong code";
        val wrongCodeException = assertThrows(BusinessException.class, () -> codeService.verifyCode(wrongCode, number, userId));
        assertEquals(ErrorMessage.WRONG_CODE.getMessage(), wrongCodeException.getMessage());
    }

    @Test
    void verifyCode_triesAreUsed() {
        val tries = 0;
        val confirmationState = new ConfirmationState(code, userId, tries, LocalDateTime.now());
        when(store.get(number)).thenReturn(confirmationState);

        val randomCode = "random code";
        val wrongCodeException = assertThrows(BusinessException.class, () -> codeService.verifyCode(randomCode, number, userId));
        assertEquals(ErrorMessage.NO_MORE_TRIES.getMessage(), wrongCodeException.getMessage());
    }

    @Test
    void verifyCode_wrongUser() {
        val confirmationState = new ConfirmationState(code, userId, Integer.MAX_VALUE, LocalDateTime.now());
        when(store.get(number)).thenReturn(confirmationState);

        val wrongUser = "wrong_user";
        val exception = assertThrows(BusinessException.class, () -> codeService.verifyCode(code, number, wrongUser));
        assertEquals(ErrorMessage.WRONG_USER.getMessage(), exception.getMessage());
    }

    @Test
    void verifyCode_numberNotFound() {
        val exception = assertThrows(BusinessException.class, () -> codeService.verifyCode(code, number, userId));
        assertEquals(ErrorMessage.NO_NUMBER_STORED.getMessage(), exception.getMessage());
    }
}