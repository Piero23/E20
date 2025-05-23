package org.unical.enterprise.authentication.dto;

public record LoginDTOResponse(
    String accessToken,
    int accessTokenExpiration,
    String refreshToken,
    int refreshTokenExpiration
) {}
