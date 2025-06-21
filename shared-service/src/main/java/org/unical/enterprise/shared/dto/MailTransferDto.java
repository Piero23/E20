package org.unical.enterprise.shared.dto;

import java.util.Date;
import java.util.UUID;

public record MailTransferDto (
        UUID ID,
        Date data,
        Double importo
){
}
