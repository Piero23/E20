package demacs.unical.esse20.config;

import demacs.unical.esse20.security.JwtConstants;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@Configuration
public class JwtKeyConfiguration {

    @Value("${jwt.secret-key.path}")
    private String jwtSecretPath;

    /* La chiave deve essere univoca e uguale ad  ogni esecuzione del servizio.
     * Ad ogni apertura, la sua versione codificata viene letta da file.
     */
    @Bean
    public SecretKey jwtSecretKey() throws Exception {
        String encoded = Files.readString(Path.of(jwtSecretPath)).trim();
        byte[] decoded = Base64.getDecoder().decode(encoded);
        return new SecretKeySpec(decoded, JwtConstants.JWS_ALGORITHM_NAME);
    }
}
