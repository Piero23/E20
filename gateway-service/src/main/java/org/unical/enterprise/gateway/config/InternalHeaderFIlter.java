package org.unical.enterprise.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class InternalHeaderFIlter {

    @Value("${internal.communication-key}")
    private String internalCommunicationKey;

    @Bean
    public GlobalFilter addInternalAuthHeader() {
        return (exchange, chain) ->
            chain.filter(
                    exchange.mutate()
                            .request(r ->
                                    r.headers(
                                    h -> {
                                        h.add("X-Internal-Communication", internalCommunicationKey);
                                        System.out.println("Sto mettendo da Gateway la comm Key");
                                    })
                            )
                            .build()
            ).then(Mono.empty());

    }
}
