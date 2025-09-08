package org.unical.enterprise.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


@SpringBootApplication
@ComponentScan(basePackages = {
		"org.unical.enterprise.auth",
		"org.unical.enterprise.shared.clients",
		"org.unical.enterprise.shared.dto",
		"org.unical.enterprise.shared.security.cors"
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
