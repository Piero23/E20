//package org.unical.enterprise.gateway.config;
//
//import lombok.AllArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@EnableWebFluxSecurity
//@RequiredArgsConstructor
//public class GatewaySecurityConfig {
//
//    private final ReactiveOAuth2AuthorizedClientService clientService;
//
//    // Preso da Shared
//    @Bean
//    public ReactiveJwtAuthenticationConverterAdapter reactiveJwtAuthenticationConverter() {
//        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
//            System.out.println("=== JWT AUTHENTICATION CONVERTER ===");
//            System.out.println("JWT Claims: " + jwt.getClaims());
//
//            // Prima prova con il formato del tuo custom auth server (claim "roles")
//            Object rolesObj = jwt.getClaim("roles");
//            System.out.println("Roles claim: " + rolesObj);
//
//            if (rolesObj instanceof List<?> roles) {
//                List<GrantedAuthority> authorities = roles.stream()
//                        .map(String::valueOf)
//                        .map(r -> {
//                            String authority = "ROLE_" + r.toUpperCase();
//                            System.out.println("Adding authority: " + authority);
//                            return new SimpleGrantedAuthority(authority);
//                        })
//                        .collect(Collectors.toList());
//
//                System.out.println("Final authorities: " + authorities);
//                return authorities;
//            }
//
//            // Se non trova ruoli, restituisce lista vuota
//            System.out.println("No roles found, returning empty list");
//            return List.of();
//        });
//
//        // Adatta il converter classico a reactive
//        return new ReactiveJwtAuthenticationConverterAdapter(converter);
//    }
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        http
//                .cors(cors -> cors.configurationSource(exchange -> new CorsConfiguration().applyPermitDefaultValues()))
//                .csrf(csrf -> csrf.disable())
//                .authorizeExchange(exchanges -> exchanges
//                        .pathMatchers("/api/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                        .anyExchange().authenticated()
//                )
//                .oauth2Login(Customizer.withDefaults())
//                .oauth2ResourceServer(resourceServer -> resourceServer
//                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(reactiveJwtAuthenticationConverter()))
//                );
//
//        return http.build();
//    }
//
//    // Filtro per inoltrare il token JWT ai microservizi
//    @Bean
//    public GlobalFilter jwtRelayFilter() {
//        return (exchange, chain) -> exchange.getPrincipal()
//                .cast(OAuth2AuthenticationToken.class)
//                .flatMap(auth -> clientService
//                        .loadAuthorizedClient(auth.getAuthorizedClientRegistrationId(), auth.getName())
//                        .flatMap(client -> {
//                            if (client != null) {
//                                exchange.getRequest()
//                                        .mutate()
//                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());
//                            }
//                            return chain.filter(exchange);
//                        })
//                );
//    }
//
//}
