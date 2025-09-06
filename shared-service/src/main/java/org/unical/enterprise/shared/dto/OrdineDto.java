package org.unical.enterprise.shared.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.UUID;

@Builder
public record OrdineDto(
        UUID utenteId,

        @NotBlank
        @Positive(message = "L'importo deve essere positivo")
        double importo
) {}
