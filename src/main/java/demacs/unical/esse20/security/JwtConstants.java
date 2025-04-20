package demacs.unical.esse20.security;

import com.nimbusds.jose.JWSAlgorithm;

public class JwtConstants {

    public static final String JWS_ALGORITHM_TAG = "HS256";
    public static final String JWS_ALGORITHM_NAME = "HmacSha256"; // Per la Generazione della Key
    public static final long JWT_KEY_LENGTH = 256; // Per la Generazione della Key

    // Token-Related
    public static final long JWT_EXPIRATION_TIME_MINUTES = 30; // Invalido dopo 30 min (temporaneo)
    public static final String JWT_TOKEN_INVALID = "TOKEN_INVALID";
}
