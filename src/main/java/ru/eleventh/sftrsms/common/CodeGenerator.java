package ru.eleventh.sftrsms.common;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CodeGenerator {

    Random random = new Random();

    public String next() {
        return String.format("%05d", random.nextInt(100_000));
    }

}
