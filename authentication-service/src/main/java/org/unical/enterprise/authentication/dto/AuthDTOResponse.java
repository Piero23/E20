package org.unical.enterprise.authentication.dto;

public record AuthDTOResponse(
        String refreshToken,
        String accessToken,
        long accessExpiration
) {
}
