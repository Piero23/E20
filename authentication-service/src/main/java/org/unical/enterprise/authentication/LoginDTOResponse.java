package org.unical.enterprise.authentication;

public record LoginDTOResponse(
    String accessToken,
    int accessTokenExpiration,
    String refreshToken,
    int refreshTokenExpiration
) {}
