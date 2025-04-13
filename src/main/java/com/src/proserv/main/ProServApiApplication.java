package com.src.proserv.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class ProServApiApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ProServApiApplication.class, args);
	}
}
