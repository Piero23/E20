package demacs.unical.esse20.dto;

import java.util.Date;


public record OrdineDto(
        String utenteId,
        int bigliettiComprati,
        double importo,
        Date dataPagamento
) {}
