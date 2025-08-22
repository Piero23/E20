package org.unical.enterprise.authentication.dto;

public record LoginDTORequest(
        String username,
        String password
){}
