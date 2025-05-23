package org.unical.enterprise.utente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"org.unical.enterprise.utente",
		"org.unical.enterprise.shared"
})
public class UtenteApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtenteApplication.class, args);
	}


}
