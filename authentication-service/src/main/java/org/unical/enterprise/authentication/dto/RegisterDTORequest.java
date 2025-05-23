package org.unical.enterprise.authentication.dto;

import jakarta.validation.constraints.Email;

public record RegisterDTORequest(
        @Email String email,
        String password,
        String username
) {}
