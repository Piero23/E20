package org.unical.enterprise.shared.security;

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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        if (securityProperties.isOpenService()) {
            return http
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .build();
        }


        http.authorizeHttpRequests(auth -> {

            if (securityProperties.getPublicPaths() != null) {
                auth.requestMatchers(securityProperties.getPublicPaths()).permitAll();
            }

            for (ProtectedRoute route : securityProperties.getProtectedRoutes()) {
                String path = route.getPath();
                String[] roles = route.getRoles();

                if (path.contains("/POST") || path.contains("/PUT") || path.contains("/DELETE")) {

                    String[] parts = path.split(":");
                    String actualPath = parts[1];
                    String method     = parts[0];


                    switch (method) {
                        case "POST"   -> auth.requestMatchers(HttpMethod.POST, actualPath).hasAnyAuthority(roles);
                        case "PUT"    -> auth.requestMatchers(HttpMethod.PUT, actualPath).hasAnyAuthority(roles);
                        case "DELETE" -> auth.requestMatchers(HttpMethod.DELETE, actualPath).hasAnyAuthority(roles);
                    }
                } else {
                    auth.requestMatchers(path).hasAnyAuthority(roles);
                }
            }

            auth.anyRequest().authenticated();
        });

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
            Object realmAccess = jwt.getClaim("realm_access");
            if (!(realmAccess instanceof Map<?,?>)) {
                return List.of();
            }
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) ((Map<String,Object>) realmAccess).get("roles");
            if (roles == null) {
                return List.of();
            }

            return roles.stream()
                    .map(r -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + r))
                    .collect(Collectors.toList());
        });
        return converter;
    }
}
