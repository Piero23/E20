package demacs.unical.esse20.dto;

import java.util.List;

public record OrdineRequest(
        OrdineDto ordine,
        List<BigliettoDto> biglietti
) {}