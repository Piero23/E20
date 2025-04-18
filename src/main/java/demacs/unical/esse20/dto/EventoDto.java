package demacs.unical.esse20.dto;

import demacs.unical.esse20.domain.Evento;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public class EventoDto {
    private Long id;
    private String nome;
    private String descrizione;
    private String organizzatore;
    private Long posti;
    private boolean b_riutilizzabile;
    private boolean b_nominativo;
    private LocationDto location;



    public EventoDto(Evento evento) {
        this.setId(evento.getId());
        this.setNome(evento.getNome());
        this.setDescrizione(evento.getDescrizione());
        this.setOrganizzatore(evento.getOrganizzatore());
        this.setPosti(evento.getPosti());
        this.setB_riutilizzabile(evento.isB_riutilizzabile());
        this.setB_nominativo(evento.isB_nominativo());
        location = new LocationDto(evento.getLocation());
    }
}
