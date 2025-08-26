package org.unical.enterprise.shared.security;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "jwt.security.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(JwtSecurityProperties.class)
@Import(CorsConfiguration.class)
@AllArgsConstructor
public class JwtSecurityAutoConfiguration {

    private final JwtSecurityProperties securityProperties;

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            @Qualifier("corsConfigurationSource") CorsConfigurationSource corsSource,
            JwtAuthFilter jwtAuthFilter
    ) throws Exception {

        http
                .cors(c -> c.configurationSource(corsSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        if (securityProperties.isOpenService()) {
            return http
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .build();
        }

        http.authorizeHttpRequests(auth -> {
            // Aggiungi endpoint pubblici per OAuth2
            auth.requestMatchers("/login/**", "/oauth2/**", "/error").permitAll();

            if (securityProperties.getPublicPaths() != null) {
                for (String path : securityProperties.getPublicPaths()) {
                    if (path.contains(":")) {
                        String[] parts = path.split(":", 2);
                        String method = parts[0];
                        String actualPath = parts[1];
                        switch (method.toUpperCase()) {
                            case "GET" -> auth.requestMatchers(HttpMethod.GET, actualPath).permitAll();
                            case "POST" -> auth.requestMatchers(HttpMethod.POST, actualPath).permitAll();
                            case "PUT" -> auth.requestMatchers(HttpMethod.PUT, actualPath).permitAll();
                            case "DELETE" -> auth.requestMatchers(HttpMethod.DELETE, actualPath).permitAll();
                        }
                    } else {
                        auth.requestMatchers(path).permitAll();
                    }
                }
            }

            if (securityProperties.getProtectedRoutes() != null) {
                for (ProtectedRoute route : securityProperties.getProtectedRoutes()) {
                    String path = route.getPath();
                    String[] roles = route.getRoles();

                    if (path.contains("POST:") || path.contains("PUT:") || path.contains("DELETE:")) {
                        String[] parts = path.split(":");
                        String actualPath = parts[1];
                        String method = parts[0];

                        switch (method) {
                            case "POST" -> auth.requestMatchers(HttpMethod.POST, actualPath).hasAnyAuthority(roles);
                            case "PUT" -> auth.requestMatchers(HttpMethod.PUT, actualPath).hasAnyAuthority(roles);
                            case "DELETE" -> auth.requestMatchers(HttpMethod.DELETE, actualPath).hasAnyAuthority(roles);
                        }
                    } else {
                        auth.requestMatchers(path).hasAnyAuthority(roles);
                    }
                }
            }

            auth.anyRequest().authenticated();
        });

        // Configurazione OAuth2 Client per il redirect automatico
        http.oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("/api/utente/test", true)
                .failureUrl("/login?error")
        );

        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
        );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            System.out.println("=== JWT AUTHENTICATION CONVERTER ===");
            System.out.println("JWT Claims: " + jwt.getClaims());

            // Prima prova con il formato del tuo custom auth server (claim "roles")
            Object rolesObj = jwt.getClaim("roles");
            System.out.println("Roles claim: " + rolesObj);

            if (rolesObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) rolesObj;
                List<GrantedAuthority> authorities = roles.stream()
                        .map(r -> {
                            String authority = "ROLE_" + r.toUpperCase();
                            System.out.println("Adding authority: " + authority);
                            return (GrantedAuthority) new SimpleGrantedAuthority(authority);
                        })
                        .collect(Collectors.toList());

                System.out.println("Final authorities: " + authorities);
                return authorities;
            }

            // Se non trova ruoli, restituisce lista vuota
            System.out.println("No roles found, returning empty list");
            return List.of();
        });
        return converter;
    }
}
