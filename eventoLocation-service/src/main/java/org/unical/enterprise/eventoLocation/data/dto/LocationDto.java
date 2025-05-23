package org.unical.enterprise.eventoLocation.data.dto;

import org.unical.enterprise.eventoLocation.data.entities.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationDto {


    public LocationDto(Location location)
    {
        this.id = location.getId();
        this.chiuso = location.isChiuso();
        this.nome = location.getNome();
        this.descrizione = location.getDescrizione();
        this.position = location.getPosition();
    }

    private long id;

    private String nome;

    private String descrizione;

    private boolean chiuso;

    private String position;


}
