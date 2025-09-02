package org.unical.enterprise.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


//    @Bean
//    public OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientService authorizedClientService) {
//
//        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder().clientCredentials().build();
//
//        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
//        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//
//        return authorizedClientManager;
//    }

    @Bean // Password Configuration
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean // Per poterlo usare manualmente nel codice all'interno dei Services
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain publicSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(
                        "/auth/register",
                        "/auth/login",
                        "/actuator/**",
                        "/auth/ciao"
                )
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfigurer authServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        http
                // Paths Per OAuth2
                .securityMatcher("/oauth2/**", "/login", "/logout", "/.well-known/**", "/userinfo")
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoint Tecnici Pubblici (Congifuragioni)
                        .requestMatchers("/.well-known/**").permitAll()

                        // Tutto il resto, Bloccato
                        .anyRequest().authenticated()
                )
                // Form Login di Base
                .formLogin(form -> form

                        // On Fail Login
                        .failureHandler((request, response, exception) -> {
                            // Se è presente una Sessione, rendila Invadlida
                            request.getSession(false);
                            if (request.getSession(false) != null) {
                                request.getSession(false).invalidate();
                            }
                            // Redirect in caso di Errore
                            response.sendRedirect("/login?error");
                        })
                )
                // Config Standard per Authorization Server
                .with(authServerConfigurer, c -> c.oidc(Customizer.withDefaults()))
                // Gestione Errori di Base: senza Autenticazione, vai a /login
                .exceptionHandling(exceptions -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                // Resource Server la Gestione dei JWT (se necessario)
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain privateSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**", "/auth/ciao/auth")
                .csrf(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(Customizer.withDefaults())
                );

        return http.build();
    }




    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer(TokenProperties properties, TokenProperties tokenProperties) {
        return context -> {

            System.out.println("\n\n" +
                    "Props:"+
//                    "\n  Issuer:" + properties.getIssuer() +
//                    "\n  Expire:" + properties.getExpiration() +
//                    "\n  Secret:" + properties.getSecret() +
                    "\n\n");

            Authentication principal = context.getPrincipal();
            if (principal == null || !context.getTokenType().getValue().equals("id_token")) return;

            // Aggiungi Campi di Riconoscimento
            context.getClaims().claim("username", principal.getName());
            context.getClaims().claim("preferred_username", principal.getName());

            // Aggiungi lo scope (per /userinfo)
            context.getClaims().claim("scope", String.join(" ", tokenProperties.getScopes()));

            // Aggiungi le Authorities
            Set<String> authorities = principal.getAuthorities()
                    .stream()
                    .map(a -> a.getAuthority().replace("ROLE_", ""))
                    .collect(Collectors.toSet());

            context.getClaims().claim("roles", authorities);

            // Impostazioni Temporali
            Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS); // Creato adesso
            context.getClaims().issuedAt(issuedAt);
            context.getClaims().notBefore(issuedAt);
            context.getClaims().expiresAt(issuedAt.plus(tokenProperties.getExpiration(), ChronoUnit.SECONDS));

            // Issuer
            context.getClaims().issuer(properties.getIssuer());


        };
    }

}
