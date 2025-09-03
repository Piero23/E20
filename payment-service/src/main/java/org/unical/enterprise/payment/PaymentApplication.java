package org.unical.enterprise.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "org.unical.enterprise.payment",
        "org.unical.enterprise.shared"
})
@EnableFeignClients(basePackages = {
        "org.unical.enterprise.payment",
        "org.unical.enterprise.shared"
})
@EnableDiscoveryClient
public class PaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);

    }

}
