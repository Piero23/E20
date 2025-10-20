package org.unical.enterprise.gateway.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

import java.util.Arrays;
import java.util.List;

/*
 * Configura le politiche di Cross-Origin Resource Sharing (CORS) per
 * permettere richieste da specifici domini esterni su Microservizi MVC.
 */
@Configuration
public class CorsWebFluxConfig {

    @Value("${cors.allowed-origins:https://localhost:8060}")
    private String originsProperty;

    @Bean
    public CorsWebFilter corsWebFilter() {
        System.out.println("=== CONFIGURING CORS ===");

        List<String> origins = Arrays.stream(originsProperty.split(","))
                .map(String::trim)
                .toList();
        System.out.println("Allowed origins: " + origins);

        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(origins); // meglio di setAllowedOrigins per gestire pattern
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "Access-Control-Allow-Origin"));
        cfg.setAllowCredentials(true);
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);

        // Solo per necessità di Costruttore viene fatto il Casting,
        // ma UrlBasedCorsConfig implementa CorsConfig
        return new CorsWebFilter((CorsConfigurationSource) src);
    }
}
