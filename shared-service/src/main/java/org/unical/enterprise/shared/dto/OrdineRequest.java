package org.unical.enterprise.shared.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record OrdineRequest(
        String mailTo,

        String nome,
        OrdineDto ordine,
        List<BigliettoDto> biglietti
){}