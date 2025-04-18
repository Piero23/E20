package demacs.unical.esse20.dto;

import demacs.unical.esse20.domain.Location;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
