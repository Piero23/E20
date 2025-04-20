package demacs.unical.esse20.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/*
 * Questo service contiene tutte le Utilities per poter utilizzare correttamte i Token JWT
 */

@Service
public class JwtTokenService {

    private final SecretKey secretKey;

    public JwtTokenService(final SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public String createToken(final String username) throws JOSEException {

        // Calcola i valori per IssuedAt, notBefore e Expiration
        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS); // Creato adesso
        Instant notBefore = issuedAt.plus(5, ChronoUnit.SECONDS); // Valido tra 5 secondi
        Instant expiration = issuedAt.plus(JwtConstants.JWT_EXPIRATION_TIME_MINUTES, ChronoUnit.MINUTES);

        // Crea i claims (campi del Token) usando le informazioni utente e applicazione
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer("Esse20.application")
                .subject(username)
                .issueTime(Date.from(issuedAt))
                .notBeforeTime(Date.from(notBefore))
                .expirationTime(Date.from(expiration))
                .build();

        // Aggiunge ai Token Claims l'Header del token, che contiene algoritmo di firma
        JWSObject jwsObject = new JWSObject(
                new JWSHeader(JWSAlgorithm.parse(JwtConstants.JWS_ALGORITHM_TAG)),
                new Payload(claims.toJSONObject())
            );

        // Firma del token utilizzando la chiave
        jwsObject.sign(new MACSigner(secretKey));

        return jwsObject.serialize();
    }

    public boolean verifyToken(final String token) throws JOSEException, ParseException  {

        try { return !getUsernameIfTokenValid(token).isBlank(); }
        catch (RuntimeException e) { return false; }
    }

    public String getUsernameIfTokenValid(final String token) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Verifica della validita' del Token
        if (!signedJWT.verify(new MACVerifier(secretKey))) { throw new RuntimeException("Token is not valid"); }

        // Verifica dei claims del Token
        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
        Instant instantNow = Instant.now();

        // Token Scaduto?
        if (claimsSet.getExpirationTime().toInstant().isBefore(instantNow)) { throw new RuntimeException("Token is expired"); }

        // Puoi gia' usare il token?
        if (claimsSet.getNotBeforeTime().toInstant().isAfter(instantNow)) { throw new RuntimeException("Token is not valid yet"); }
        if (claimsSet.getIssueTime().toInstant().isAfter(instantNow)) { throw new RuntimeException("Token issued for later use"); }

        return claimsSet.getSubject();
    }

    public String getToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer "))
            return header.substring(7); // Per eliminare "Bearer "
        return JwtConstants.JWT_TOKEN_INVALID;
    }



}
