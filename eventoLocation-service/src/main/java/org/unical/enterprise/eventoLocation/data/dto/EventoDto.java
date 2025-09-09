package org.unical.enterprise.eventoLocation.data.dto;

import org.unical.enterprise.eventoLocation.data.entities.Evento;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor

public class EventoDto {

    private Long id;
    private String nome;
    private String descrizione;
    private UUID organizzatore;
    private Long posti;
    private boolean b_riutilizzabile;
    private boolean b_nominativo;
    private boolean age_restricted;
    private LocationDto location;
    private Date data;
    private double prezzo;

    //TODO ToDTO

    public EventoDto(Evento evento) {
        this.setId(evento.getId());
        this.setNome(evento.getNome());
        this.setDescrizione(evento.getDescrizione());
        this.setOrganizzatore(evento.getOrganizzatore());
        this.setPosti(evento.getPosti());
        this.setB_riutilizzabile(evento.isB_riutilizzabile());
        this.setB_nominativo(evento.isB_nominativo());
        this.setAge_restricted(evento.isAge_restricted());
        this.setData(evento.getData());
        this.setPrezzo(evento.getPrezzo());

        try {location = new LocationDto(evento.getLocation());}
        catch (Exception e){location = null;}
    }
}
