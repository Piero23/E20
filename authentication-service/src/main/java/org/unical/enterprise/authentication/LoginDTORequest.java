package org.unical.enterprise.authentication;

import jakarta.validation.constraints.Email;

public record LoginDTORequest(
        @Email String mail,
        String password
){}
