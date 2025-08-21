package org.unical.enterprise.utente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"org.unical.enterprise.utente",
		"org.unical.enterprise.shared"
})

@EnableDiscoveryClient
@EnableFeignClients
public class UtenteApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtenteApplication.class, args);
	}


}
