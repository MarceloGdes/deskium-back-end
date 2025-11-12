package br.deskiumcompany.deskium_ai_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//Ativando metodos asincronos https://spring.io/guides/gs/async-method
@EnableAsync
public class DeskiumAiApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeskiumAiApiApplication.class, args);
	}

}
