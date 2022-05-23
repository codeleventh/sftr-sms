package ru.eleventh.sftrsms.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.eleventh.sftrsms.common.CodeGenerator;
import ru.eleventh.sftrsms.error.BusinessException;
import ru.eleventh.sftrsms.error.ErrorMessage;

@Service
@RequiredArgsConstructor
public class CodeService {

    private final Store store;

    private final CodeGenerator generator;

    private final MessageService messageService;

    public void sendCode(@NonNull String phoneNumber, @NonNull String userId) {
        val storedNumberInfo = store.get(phoneNumber);
        if (storedNumberInfo != null) {
            throw new BusinessException(ErrorMessage.ALREADY_HAVE.getMessage());
        }

        val code = generator.next();
        messageService.sendMessage(code, phoneNumber);
        store.add(phoneNumber, userId, code);

    }

    public void verifyCode(@NonNull String code, @NonNull String phoneNumber, @NonNull String userId) {
        val confirmationState = store.get(phoneNumber);

        if (confirmationState == null) {
            throw new BusinessException(ErrorMessage.NO_NUMBER_STORED.getMessage());
        } else if (!userId.equals(confirmationState.getUserId())) {
            throw new BusinessException(ErrorMessage.WRONG_USER.getMessage());
        } else if (!code.equals(confirmationState.getCode())) {
            val tries = confirmationState.getTries();
            if (tries <= 0)
                throw new BusinessException(ErrorMessage.NO_MORE_TRIES.getMessage());
            else confirmationState.setTries(tries - 1);

            throw new BusinessException(ErrorMessage.WRONG_CODE.getMessage());
        } else store.remove(phoneNumber);
    }
}
