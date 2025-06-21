package org.unical.enterprise.shared.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;

import java.util.Date;
import java.util.UUID;

public record MailTransferDto (
        UUID ID,
        Date data,
        @Positive Double importo,
        @Email String mail
){
}
