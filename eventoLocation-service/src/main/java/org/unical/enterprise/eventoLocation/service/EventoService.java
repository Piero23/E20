package org.unical.enterprise.eventoLocation.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.unical.enterprise.eventoLocation.ContentNotFoundException;
import org.unical.enterprise.eventoLocation.data.dao.EventoDao;
import org.unical.enterprise.eventoLocation.data.dao.LocationDao;
import org.unical.enterprise.eventoLocation.data.dto.EventoBasicDto;
import org.unical.enterprise.eventoLocation.data.dto.EventoDto;
import org.unical.enterprise.eventoLocation.data.entities.Evento;
import org.unical.enterprise.eventoLocation.data.entities.Location;

@Service
@AllArgsConstructor
public class EventoService {


    EventoDao eventoDao;
    LocationDao locationDao;


    public Page<EventoBasicDto> getPagable(Pageable pageable){
        Page<Evento> eventos = eventoDao.findAll(pageable);

        Page<EventoBasicDto> basic = eventos.map(evento -> new EventoBasicDto(evento));


        return basic;
    }


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