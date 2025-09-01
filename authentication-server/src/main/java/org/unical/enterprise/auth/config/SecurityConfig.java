package org.unical.enterprise.auth.config;

import jakarta.security.auth.message.config.AuthConfig;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.unical.enterprise.auth.service.CustomUserDetailsService;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Password Configuration
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    // Per poterlo usare manualmente nel codice all'interno dei Services
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    @Order(1) // Prima chain: controlla solo i percorsi pubblici
    public SecurityFilterChain publicApiSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                // Qui dichiariamo quali percorsi questa chain deve gestire
                .securityMatcher("/auth/register")

                // Disabilitiamo CSRF perché è una POST da client (es. gateway o Postman)
                .csrf(csrf -> csrf.disable())

                // Permetti l’accesso a tutti senza autenticazione
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                );

        return http.build();
    }



//    @Bean
//    @Order(1) // Security Filter Chain per Paths Pubblici (via Gateway)
//    public SecurityFilterChain publicApiSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                // Solo gli Endpoint nascosti dal Gateway
//                .securityMatcher("/auth/register")
//                // Disabitia i CSRF cosi' non da 403 per Invalid CSRF token
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(authorize -> authorize
//                        .anyRequest().permitAll()
//                );
//
//        return http.build();
//    }

//    @Bean
//    @Order(1) // Security Filter Chain con OAuth2
//    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                .securityMatcher("/oauth2/**", "/login", "/logout", "/.well-known/**")
//                .authorizeHttpRequests(authorize -> authorize
//                        // OAuth2 Technical Endpoints
//                        .requestMatchers("/oauth2/**", "/.well-known/**").permitAll()
//
//                        // Tutte le altre Routs Protette
//                        .anyRequest().authenticated()
//                )
//                // Config Standard per Authentication-Server
//                .with(new OAuth2AuthorizationServerConfigurer(), configurer ->
//                        configurer.oidc(Customizer.withDefaults())
//                )
//                // Redirect alla Pagina di Login per Authentication-Server
//                .exceptionHandling(exceptions -> exceptions
//                        .defaultAuthenticationEntryPointFor(
//                                new LoginUrlAuthenticationEntryPoint("/login"),
//                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
//                        )
//                )
//                // Controllo JWT sugli Endpoints
//                .oauth2ResourceServer(resourceServer -> resourceServer
//                        .jwt(Customizer.withDefaults())
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    @Order(2)
//    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher("/auth/**", "/api/**")
//                .csrf(crsf -> crsf.ignoringRequestMatchers("/auth/register"))
//                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers("/auth/register").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(Customizer.withDefaults());
//
//        return http.build();
//    }


    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return context -> {
            Authentication principal = context.getPrincipal();

            if (context.getPrincipal() != null) {

                Set<String> authorities = principal.getAuthorities()
                        .stream()
                        .map(a -> a.getAuthority().replace("ROLE_", ""))
                        .collect(Collectors.toSet());

                context.getClaims().claim("roles", authorities);
                context.getClaims().claim("username", principal.getName());

            }
        };
    }

}
