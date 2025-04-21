package demacs.unical.esse20.service;


import demacs.unical.esse20.ContentNotFoundException;
import demacs.unical.esse20.data.dao.EventoDao;
import demacs.unical.esse20.data.dao.LocationDao;
import demacs.unical.esse20.data.dto.EventoBasicDto;
import demacs.unical.esse20.data.dto.EventoDto;
import demacs.unical.esse20.data.entities.Evento;
import demacs.unical.esse20.data.entities.Location;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class EventoService {


    EventoDao eventoDao;
    LocationDao locationDao;

    public EventoBasicDto getByIdNoLocation(Long id){
        return new EventoBasicDto(eventoDao.findById(id).orElseThrow(() ->
                new ContentNotFoundException("Event with id " + id + " not found")
        ));
    }

    public EventoDto getByIdWithLocation(Long id){
        return new EventoDto(eventoDao.findById(id).orElseThrow(() ->
                new ContentNotFoundException("Event with id " + id + " not found")
        ));
    }

    public EventoBasicDto save(EventoBasicDto dto){

        Evento evento = new Evento();

        evento.setNome(dto.getNome());
        evento.setDescrizione(dto.getDescrizione());
        evento.setOrganizzatore(dto.getOrganizzatore());
        evento.setPosti(dto.getPosti());
        evento.setB_riutilizzabile(dto.isB_riutilizzabile());
        evento.setB_nominativo(dto.isB_nominativo());
        evento.setData(dto.getData());

        Location location = locationDao.findById(dto.getLocationId())
                .orElseThrow(() -> new ContentNotFoundException("location with id " + dto.getLocationId() + " not found"));

        evento.setLocation(location);

        return new EventoBasicDto(eventoDao.save(evento));
    }

    public Evento update(Evento evento , Long id){

        eventoDao.findById(id).orElseThrow(() ->
                new ContentNotFoundException("location with id " + id + " not found")
        );

        evento.setId(id);

        return eventoDao.save(evento);
    }

    public void delete(Long id){

        if(eventoDao.findById(id).isEmpty())
            throw new ContentNotFoundException("event with id " + id + " not found");

        eventoDao.deleteById(id);
    }




}