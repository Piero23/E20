package org.unical.enterprise.gestioneOrdini.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.UUID;


public record OrdineDto(
        UUID utenteId,

        @NotBlank
        @Positive(message = "L'importo deve essere positivo")
        double importo
) {}
