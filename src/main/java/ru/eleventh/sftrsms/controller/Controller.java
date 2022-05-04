package ru.eleventh.sftrsms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import ru.eleventh.sftrsms.dto.Response;
import ru.eleventh.sftrsms.dto.SendRequest;
import ru.eleventh.sftrsms.dto.VerifyRequest;
import ru.eleventh.sftrsms.error.BusinessException;
import ru.eleventh.sftrsms.error.ErrorMessage;
import ru.eleventh.sftrsms.service.CodeService;

import javax.validation.Valid;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class Controller {
    private final CodeService codeService;

    @PostMapping("send")
    public ResponseEntity<?> sendCode(@RequestBody @Valid SendRequest request) {
        codeService.sendCode(request.getPhoneNumber(), request.getUserId());
        return ResponseEntity.ok(Response.ok());
    }

    @PostMapping("verify")
    public ResponseEntity<?> verifyCode(@RequestBody @Valid VerifyRequest request) {
        codeService.verifyCode(request.getCode(), request.getPhoneNumber(), request.getUserId());
        return ResponseEntity.ok(Response.ok());
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<?> handleValidationException() {
        return ResponseEntity.ok(Response.of(ErrorMessage.WRONG_FORMAT));
    }

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<?> handleException() {
        return ResponseEntity.ok(Response.of(ErrorMessage.DEFAULT));
    }

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<?> handleBusinessException(Exception e) {
        return ResponseEntity.ok(Response.of(e.getMessage()));
    }
}
