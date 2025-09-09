package org.unical.enterprise.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {
		"org.unical.enterprise.auth",
		"org.unical.enterprise.shared.clients",
		"org.unical.enterprise.shared.dto",
        "org.unical.enterprise.shared.security.internal"
})
@EnableFeignClients(basePackages = {
		"org.unical.enterprise.shared.clients"
})
@EnableDiscoveryClient
public class AuthServerApplication {

    //TODO fare in modo che admin abbia tutti i ruoli sottostanti e anche manager

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}

}
