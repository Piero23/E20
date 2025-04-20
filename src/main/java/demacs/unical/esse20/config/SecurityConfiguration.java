package demacs.unical.esse20.config;

import demacs.unical.esse20.security.JwtAuthRequestFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthRequestFilter jwtAuthRequestFilter;

    public SecurityConfiguration(JwtAuthRequestFilter jwtAuthRequestFilter) {
        this.jwtAuthRequestFilter = jwtAuthRequestFilter;
    }

    // Password Encoder: usato per codificare le password (JWT), sale fissato: 12
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // Autorizzazioni sugli End-point
                .authorizeHttpRequests(
                    auth -> {
                        // Aperti a tutti
                        auth.requestMatchers(
                                "/api/v1/register",
                                "/api/v1/authenticate"
                             ).permitAll();
                        // Bloccati, accesso previa autorizzazione
                        auth.anyRequest().authenticated();
                    })
                // Disattiva il controllo per Cross-Site Request Forgery,
                // le richieste con JWT non sono fatte tramite cookies o sessioni
                .csrf(csrf_ -> csrf_.disable())
                // Sessione Stateless quindi ad ogni chiamata il token deve essere reinviato
                .sessionManagement(mng -> mng.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Aggiunta del filtro per le richieste con Token JWT e poi quello di tipo Username-Password
                // Cosi' prima viene intereccetato il token che, se presente, autentica l'utente
                .addFilterBefore(jwtAuthRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
