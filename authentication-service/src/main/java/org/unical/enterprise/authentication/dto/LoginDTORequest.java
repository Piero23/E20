package org.unical.enterprise.authentication.dto;

import jakarta.validation.constraints.Email;

public record LoginDTORequest(
        @Email String mail,
        String password
){}
