package org.unical.enterprise.shared.clients;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FeignConfig {
    @Value("${internal.communication-key}")
    private String internalCommunicationKey;

    @Bean
    public RequestInterceptor internalRequestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("X-Internal-Request", "true");
            System.out.println("Ciao carissimi, sto mettendo tramite feign la comm Key");
            requestTemplate.header("X-Internal-Communication", internalCommunicationKey);
            System.out.println("Headers Feign:");
            requestTemplate.headers().forEach((key, value) -> System.out.println(key + " : " + String.join(",", value)));
        };
    }
}
