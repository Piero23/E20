package org.unical.enterprise.gestioneOrdini.dto;

import org.unical.enterprise.shared.dto.BigliettoDto;

import java.util.List;

public record OrdineRequest(
        OrdineDto ordine,
        List<BigliettoDto> biglietti
) {}