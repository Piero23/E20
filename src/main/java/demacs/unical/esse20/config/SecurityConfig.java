package demacs.unical.esse20.config;

import demacs.unical.esse20.service.UtenteDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UtenteDetailsService utenteDetailsService;

    public SecurityConfig(UtenteDetailsService utenteDetailsService) {
        this.utenteDetailsService = utenteDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disabilita la protezione CSRF (Cross-Site Request Forgery) (Non so cosa sia)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/register", "/login").permitAll() // Permette l'accesso a queste rotte senza autenticazione
                .anyRequest().permitAll() // FIXME -- Da cambiare in authenticated() quando si implementerà la sicurezza
            )
                .formLogin().disable(); // Disabilita il form di login predefinito di Spring Security
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // FIXME -- ATTENZIONE! PERICOLO DI SICUREZZA!
    }
}
