package org.unical.enterprise.gestioneOrdini.dto;

import jakarta.validation.constraints.Email;

import java.util.Date;

public record BigliettoDto(
        Long idEvento,
        @Email String email,
        boolean eValido,
        String nome,
        String cognome,
        Date dataNascita
) {}
