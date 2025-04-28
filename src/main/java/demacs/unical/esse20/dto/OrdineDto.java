package demacs.unical.esse20.dto;

import java.util.Date;
import java.util.UUID;


public record OrdineDto(
        UUID utenteId,
        int bigliettiComprati,
        double importo,
        Date dataPagamento
) {}
