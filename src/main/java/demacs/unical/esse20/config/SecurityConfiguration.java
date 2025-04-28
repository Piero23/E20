package demacs.unical.esse20.config;

import demacs.unical.esse20.security.JwtTokenService;
import demacs.unical.esse20.security.oauth2.CustomOAuth2FailureHandler;
import demacs.unical.esse20.security.oauth2.CustomOAuth2SuccessHandler;
import demacs.unical.esse20.security.JwtAuthRequestFilter;

import demacs.unical.esse20.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthRequestFilter jwtAuthRequestFilter;

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
    public AuthenticationFailureHandler oAuth2FailureHandler() {
        return new CustomOAuth2FailureHandler();
    }

    @Bean
    public AuthenticationSuccessHandler oAuth2SuccessHandler(UtenteService utenteService, JwtTokenService jwtTokenService) {
        return new CustomOAuth2SuccessHandler(utenteService, jwtTokenService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationSuccessHandler oAuth2SuccessHandler,
                                           AuthenticationFailureHandler oAuth2FailureHandler) throws Exception {
        return http
                // Quando si effettua il log-out viene invalidata la sessione ed eliminato il token
                .logout(
                lgt -> {
                    lgt.permitAll();
                    lgt.invalidateHttpSession(true);
                    lgt.clearAuthentication(true);
                    lgt.deleteCookies("JSESSIONID");
                })
                // Autorizzazioni sugli End-point
                .authorizeHttpRequests(
                        auth -> {
                            // Aperti a tutti
                            auth.requestMatchers(
                                    "/login",
                                    "/api/v1/auth/**",
                                    "/oauth2/authorization/**"
                            ).permitAll();
                            // Bloccati, accesso previa autorizzazione
                            auth.anyRequest().authenticated();
                        })
                // Disattiva il controllo per Cross-Site Request Forgery,
                // le richieste con JWT non sono fatte tramite cookies o sessioni
                .csrf(AbstractHttpConfigurer::disable)
                // Sessione Stateless quindi ad ogni chiamata il token deve essere reinviato
                .sessionManagement(mng -> mng.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Delega l'autenticazione dell'utente a servizi esterni tramite OAuth2 per poi generare il token
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                )
                // Aggiunta del filtro per le richieste con Token JWT e poi quello di tipo Username-Password
                // Cosi' prima viene intereccetato il token che, se presente, autentica l'utente
                .addFilterBefore(jwtAuthRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}
