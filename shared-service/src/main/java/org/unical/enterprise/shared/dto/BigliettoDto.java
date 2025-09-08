package org.unical.enterprise.shared.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.Date;

@Builder
public record BigliettoDto(

        @NotBlank
        Long idEvento,

        @NotBlank(message = "Il campo email non può essere vuoto.")
        @Email(message = "L'indirizzo email non è valido.")
        String email,

        boolean eValido,

        String nome,

        String cognome,

        Date dataNascita
) {}
