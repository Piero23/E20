package demacs.unical.esse20.security.oauth2;

import com.nimbusds.jose.JOSEException;
import demacs.unical.esse20.data.domain.AuthProvider;
import demacs.unical.esse20.data.domain.Utente;
import demacs.unical.esse20.data.dto.UtenteRegistrationDto;
import demacs.unical.esse20.security.JwtTokenService;
import demacs.unical.esse20.service.UtenteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UtenteService utenteService;
    private final JwtTokenService jwtTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {

        // L'utente proviene dal Serivizo di OAuth2 Authentication
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // Cerca l'utente altrimenti lo crea e lo salva
        String userEmail = oAuth2User.getAttribute("email");

        Utente user = utenteService.getUserByEmail(userEmail).orElseGet(() -> {
            assert userEmail != null;

            // Avere il token OAuth2 mi permette di capire quale Provider sta facendo l'operazione
            OAuth2AuthenticationToken oAuthToken = (OAuth2AuthenticationToken) authentication;
            String providerId = oAuthToken.getAuthorizedClientRegistrationId();

            UtenteRegistrationDto userReg = UtenteRegistrationDto.builder()
                    .username(userEmail.substring(0, userEmail.indexOf("@")))
                    .email(userEmail)
                    .password("")
                    .dataNascita(String.valueOf(LocalDate.now()))
                    .build();

            return utenteService.registerNewUser(userReg, AuthProvider.asProvider(providerId));
        });

        // Crea il token per il nuovo utente
        try {
            String token = jwtTokenService.createToken(user.getUsername());

            // Salva il token per l'utente nel campo Authorization della response
            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (JOSEException e) { throw new RuntimeException("Non e' stato possibile creare il Token JWT"); }
    }
}
