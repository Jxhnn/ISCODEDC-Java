package com.gamesUP.gamesUP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class GamesUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(GamesUpApplication.class, args);
	}

}
