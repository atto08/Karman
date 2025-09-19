package com.project.Karman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KarmanApplication {

	public static void main(String[] args) {
		SpringApplication.run(KarmanApplication.class, args);
	}

}
