package org.unical.enterprise.eventoLocation.data.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.unical.enterprise.eventoLocation.data.entities.Preferiti;

@Getter
@Setter
@NoArgsConstructor
public class PreferitiDto {
    long id;
    String utente_id;
    long evento_id;

    //TODO ToDTO
    public PreferitiDto(Preferiti preferiti) {
        this.id = preferiti.getId();
        this.utente_id = preferiti.getUtente_id();
        this.evento_id = preferiti.getEvento().getId();
    }
}
