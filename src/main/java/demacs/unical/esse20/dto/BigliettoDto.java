package demacs.unical.esse20.dto;

import java.util.Date;

public record BigliettoDto(
        Long idEvento,
        String email,
        boolean eValido,
        String nome,
        String cognome,
        Date dataNascita
) {}
