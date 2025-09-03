package org.unical.enterprise.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.server.WebSession;

import java.net.URI;
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

//    @Bean
//    public ReactiveJwtDecoder jwtDecoder(TokenProperties tokenProperties) {
//        System.out.println(tokenProperties.getSecret());
//
//        SecretKey secretKey = Keys.hmacShaKeyFor(
//                Base64.getUrlDecoder().decode(tokenProperties.getSecret())
//        );
//
//        return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
//    }

    @Bean
    public ServerRequestCache requestCache() {
        return new WebSessionServerRequestCache();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         ServerRequestCache requestCache,
                                                         ReactiveJwtAuthenticationConverterAdapter jwtAuthConverter)
    {
        http
                .cors(cors -> cors.configurationSource(exchange -> new CorsConfiguration().applyPermitDefaultValues()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // Endpoint Tecnici
                        .pathMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Endpoint Registrazione, Autenticazione Stateless
                        .pathMatchers("/auth/register", "/auth/login").permitAll()

                        .anyExchange().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        // On Success -> Go to the Original Requested URL
                        .authenticationSuccessHandler((webFilterExchange, authentication) ->

                                // Recupera l'URL iniziale
                                requestCache.getRedirectUri(webFilterExchange.getExchange())
                                        .defaultIfEmpty(URI.create("/api/gateway/me")) // Temporaneo -> utente/me
                                        .flatMap(uri -> {
                                            var response = webFilterExchange.getExchange().getResponse();

                                            // Segnala il FOUND della Richiesta
                                            response.setStatusCode(HttpStatus.FOUND);

                                            // Rimanda all'URL Originale della Richiesta
                                            response.getHeaders().setLocation(uri);

                                            // Concludi il Processo di Login + Reindizzamento
                                            return response.setComplete();
                                        })
                        )

                        // On Fail Authorization -> Invalidate Session
                        .authenticationFailureHandler((webFilterExchange, exception) ->

                                webFilterExchange.getExchange()
                                    // Invalida la Sessione Corrente
                                    .getSession()
                                    .doOnNext(WebSession::invalidate)
                                    .flatMap(session -> {
                                        // Rimanda Unauthorizaed
                                        webFilterExchange.getExchange()
                                                .getResponse()
                                                .setStatusCode(HttpStatus.UNAUTHORIZED);
                                        // Concludi il Processo di Fallimento
                                        return webFilterExchange.getExchange()
                                                .getResponse()
                                                .setComplete();
                                    })
                        )
                )
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(jwtAuthConverter))
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable);

        return http.build();
    }

}
