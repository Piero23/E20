package org.unical.enterprise.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.unical.enterprise.gateway.config.RateLimiterProperties;

@SpringBootApplication
@EnableDiscoveryClient
@RequiredArgsConstructor

//TODO Questa annotazione andrebbe nel main dell'applicazione, per ora la metto qui.
@EnableConfigurationProperties(RateLimiterProperties.class)
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
