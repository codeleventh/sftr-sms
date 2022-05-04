package ru.eleventh.sftrsms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SftrSmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SftrSmsApplication.class, args);
	}

}
