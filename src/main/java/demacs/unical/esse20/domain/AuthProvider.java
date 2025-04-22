package demacs.unical.esse20.domain;


public enum AuthProvider {
    LOCAL,
    GOOGLE;

    public static AuthProvider asProvider(String authProviderName) {
        try {
            return AuthProvider.valueOf(authProviderName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Provider OAuth2 sconosciuto: " + authProviderName);
        }
    }
}