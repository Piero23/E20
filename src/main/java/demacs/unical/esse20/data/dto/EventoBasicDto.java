package demacs.unical.esse20.data.dto;

import demacs.unical.esse20.data.entities.Evento;
import jdk.jfr.Event;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class EventoBasicDto {
    private Long id;
    private String nome;
    private String descrizione;
    private String organizzatore;
    private Long posti;
    private boolean b_riutilizzabile;
    private boolean b_nominativo;
    private Long locationId;
    private Date data;
    private double prezzo;

    public EventoBasicDto(Evento event) {
        this.id = event.getId();
        this.nome = event.getNome();
        this.descrizione = event.getDescrizione();
        this.organizzatore = event.getOrganizzatore();
        this.posti = event.getPosti();
        this.data = event.getData();
        this.prezzo = event.getPrezzo();

        try {
            this.locationId = event.getLocation().getId();
        }catch(Exception e){
            locationId = null;
        }
    }
}
