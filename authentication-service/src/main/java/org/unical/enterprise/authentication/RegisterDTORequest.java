package org.unical.enterprise.authentication;

import jakarta.validation.constraints.Email;

public record RegisterDTORequest(
        @Email String email,
        String password,
        String username
) {}
