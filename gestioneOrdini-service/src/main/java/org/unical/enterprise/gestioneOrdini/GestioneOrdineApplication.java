package org.unical.enterprise.gestioneOrdini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"org.unical.enterprise.gestioneOrdini",
		"org.unical.enterprise.shared"
})
@EnableFeignClients(basePackages = {
		"org.unical.enterprise.shared.clients"
})
@EnableDiscoveryClient
public class GestioneOrdineApplication {
	public static void main(String[] args) {
		SpringApplication.run(GestioneOrdineApplication.class, args);

	}

}
