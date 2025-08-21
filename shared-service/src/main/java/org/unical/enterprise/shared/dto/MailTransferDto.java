package org.unical.enterprise.shared.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.Date;
import java.util.UUID;

public record MailTransferDto (
        UUID ID,

        Date data,

        @NotBlank
        @Positive(message = "L'importo deve essere positivo")
        Double importo,

        @NotBlank(message = "Il campo email non può essere vuoto.")
        @Email(message = "L'indirizzo email non è valido.")
        String mail,

        @NotBlank(message = "Il campo cliente non può essere vuoto.")
        String cliente
){
}
