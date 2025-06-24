package org.unical.enterprise.shared.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/*
 * Configura automaticamente la sicurezza in ogni microservizio in base alle proprietà definite nei file YAML.
 */
@Configuration
@ConditionalOnProperty(name = "jwt.security.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(JwtSecurityProperties.class)
@Import({JwtServiceConfiguration.class, CorsConfiguration.class})
public class JwtSecurityAutoConfiguration {

    private final JwtSecurityProperties securityProperties;
    private final JwtService jwtService;

    public JwtSecurityAutoConfiguration(JwtSecurityProperties securityProperties, JwtService jwtService) {
        this.securityProperties = securityProperties;
        this.jwtService = jwtService;
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtService);
    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http,
                                          JwtAuthFilter jwtAuthFilter, 
                                          CorsConfigurationSource corsConfigurationSource) throws Exception {
        
        // Se il servizio è contrassegnato come "open", permette tutti gli accessi
        if (securityProperties.isOpenService()) {
            return http
                    .cors(c -> c.configurationSource(corsConfigurationSource))
                    .csrf(csrf -> csrf.disable())
                    .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .build();
        }

        System.out.println("MUCCA");
        if (securityProperties.getProtectedRoutes() != null) {
            for (ProtectedRoute route : securityProperties.getProtectedRoutes()) {
                String path = route.getPath();
                String[] roles = route.getRoles();

                // Controlla se il path contiene indicazione del metodo HTTP
                if (path.contains("/GET") || path.contains("/POST") || path.contains("/PUT") || path.contains("/DELETE")) {
                    String actualPath = path.split("/")[0];
                    String method = path.split("/")[1];

                    System.out.println(actualPath);
                    System.out.println(method);
                }
            }
        }
        System.out.println(securityProperties);

        // Altrimenti, configura la sicurezza in base alle rotte definite
        return http
                .cors(c -> c.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    // Configura le rotte pubbliche
                    if (securityProperties.getPublicPaths() != null && securityProperties.getPublicPaths().length > 0) {
                        auth.requestMatchers(securityProperties.getPublicPaths()).permitAll();
                    }

                    // Configurazione delle rotte protette,
                    if (securityProperties.getProtectedRoutes() != null) {
                        for (ProtectedRoute route : securityProperties.getProtectedRoutes()) {
                            String path = route.getPath();
                            String[] roles = route.getRoles();

                            // Controlla se il path contiene indicazione del metodo HTTP
                            if (path.contains("/GET") ||path.contains("/POST") || path.contains("/PUT") || path.contains("/DELETE")) {
                                String actualPath = path.split("/")[0];
                                String method = path.split("/")[1];

                                switch(method) {
                                    case "GET":
                                        auth.requestMatchers(HttpMethod.GET, actualPath).hasAnyAuthority(roles);
                                        break;
                                    case "POST":
                                        auth.requestMatchers(HttpMethod.POST, actualPath).hasAnyAuthority(roles);
                                        break;
                                    case "PUT":
                                        auth.requestMatchers(HttpMethod.PUT, actualPath).hasAnyAuthority(roles);
                                        break;
                                    case "DELETE":
                                        auth.requestMatchers(HttpMethod.DELETE, actualPath).hasAnyAuthority(roles);
                                        break;
                                }
                            } else {
                                auth.requestMatchers(path).hasAnyAuthority(roles);
                            }
                        }
                    }
                    
                    // Per default, tutte le altre rotte richiedono autenticazione
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}