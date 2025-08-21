package org.unical.enterprise.mailSender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"org.unical.enterprise.mailSender",
		"org.unical.enterprise.shared"
})
@EnableFeignClients
@EnableDiscoveryClient
public class MailSenderApplication {


	public static void main(String[] args) {
		SpringApplication.run(MailSenderApplication.class, args);
	}

}
