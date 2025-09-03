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

        @NotBlank(message = "Inserire Nome")
        String nome,

        @NotBlank(message = "Inserire Cognome")
        String cognome,

        Date dataNascita
) {}
