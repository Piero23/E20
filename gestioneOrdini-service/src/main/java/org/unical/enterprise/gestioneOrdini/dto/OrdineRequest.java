package org.unical.enterprise.gestioneOrdini.dto;

import java.util.List;

public record OrdineRequest(
        OrdineDto ordine,
        List<BigliettoDto> biglietti
) {}