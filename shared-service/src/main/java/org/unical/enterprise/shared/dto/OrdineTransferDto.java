package org.unical.enterprise.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
public record OrdineTransferDto (
    UUID utenteId,
    String valuta,
    List<BigliettoDto> biglietti
) {}
