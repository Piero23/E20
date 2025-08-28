package org.unical.enterprise.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    // Ispirato da Shared - JWT Authentication Converter
    @Bean
    public ReactiveJwtAuthenticationConverterAdapter reactiveJwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            System.out.println("=== JWT AUTHENTICATION CONVERTER - REACTIVE ===");
            System.out.println("JWT Claims: " + jwt.getClaims());

            // Prima prova con il formato del tuo custom auth server (claim "roles")
            Object rolesObj = jwt.getClaim("roles");
            System.out.println("Roles claim: " + rolesObj);

            if (rolesObj instanceof List<?> roles) {
                List<GrantedAuthority> authorities = roles.stream()
                        .map(String::valueOf)
                        .map(r -> {
                            String authority = "ROLE_" + r.toUpperCase();
                            System.out.println("Adding authority: " + authority);
                            return new SimpleGrantedAuthority(authority);
                        })
                        .collect(Collectors.toList());

                System.out.println("Final authorities: " + authorities);
                return authorities;
            }

            // Se non trova ruoli, restituisce lista vuota
            System.out.println("No roles found, returning empty list");
            return List.of();
        });

        // Adatta il converter classico a reactive
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .cors(cors -> cors.configurationSource(exchange -> new CorsConfiguration().applyPermitDefaultValues()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2Login(Customizer.withDefaults())
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(jwtSpec ->
                                jwtSpec.jwtAuthenticationConverter(reactiveJwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

}
