package org.unical.enterprise.eventoLocation.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.unical.enterprise.eventoLocation.ContentNotFoundException;
import org.unical.enterprise.eventoLocation.data.dao.EventoDao;
import org.unical.enterprise.eventoLocation.data.dao.LocationDao;
import org.unical.enterprise.eventoLocation.BigliettoServiceClient;
import org.unical.enterprise.shared.clients.UtenteServiceClient;
import org.unical.enterprise.shared.dto.BigliettoDto;
import org.unical.enterprise.shared.dto.EventoBasicDto;
import org.unical.enterprise.eventoLocation.data.dto.EventoDto;
import org.unical.enterprise.eventoLocation.data.entities.Evento;
import org.unical.enterprise.eventoLocation.data.entities.Location;
import org.unical.enterprise.shared.dto.UtenteDTO;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EventoService {


    EventoDao eventoDao;
    LocationDao locationDao;
    UtenteServiceClient utenteServiceClient;
    BigliettoServiceClient bigliettoServiceClient;


    public Page<EventoBasicDto> getPagable(Pageable pageable){
        Page<Evento> eventos = eventoDao.findAll(pageable);

        Page<EventoBasicDto> basic = eventos.map(evento -> toDTO(evento));


        return basic;
    }


    public EventoBasicDto getByIdNoLocation(Long id){
        return  toDTO(eventoDao.findById(id).orElseThrow(() ->
                new ContentNotFoundException("Event with id " + id + " not found")
        ));
    }

    public EventoDto getByIdWithLocation(Long id){
        return new EventoDto(eventoDao.findById(id).orElseThrow(() ->
                new ContentNotFoundException("Event with id " + id + " not found")
        ));
    }

    @Transactional
    public EventoBasicDto save(EventoBasicDto dto){
        System.out.println("Palle ciao pure qua");
        System.out.println("dto = " + dto);

        Evento evento = new Evento();

        evento.setNome(dto.getNome());
        evento.setDescrizione(dto.getDescrizione());

        UtenteDTO u = utenteServiceClient.getById(dto.getOrganizzatore());
        System.out.println(u);


        evento.setOrganizzatore(dto.getOrganizzatore());
        evento.setPosti(dto.getPosti());
        evento.setB_riutilizzabile(dto.isB_riutilizzabile());
        evento.setB_nominativo(dto.isB_nominativo());
        evento.setAge_restricted(dto.isAge_restricted());
        evento.setData(dto.getData());
        evento.setImmagine(dto.getImmagine());

        Location location = locationDao.findById(dto.getLocationId())
                .orElseThrow(() -> new ContentNotFoundException("location with id " + dto.getLocationId() + " not found"));

        evento.setLocation(location);

        return toDTO(eventoDao.save(evento));
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

    public EventoBasicDto toDTO(Evento evento){
        return  EventoBasicDto.builder().
                id(evento.getId()).
                nome(evento.getNome()).
                descrizione(evento.getDescrizione()).
                organizzatore(evento.getOrganizzatore()).
                posti(evento.getPosti()).
                b_riutilizzabile(evento.isB_riutilizzabile()).
                b_nominativo(evento.isB_nominativo()).
                age_restricted(evento.isAge_restricted()).
                locationId(evento.getId()).
                data(evento.getData()).
                prezzo(evento.getPrezzo()).build();
    }

    public List<BigliettoDto> getBigliettiByEvento(Long id){
        return bigliettoServiceClient.getBigliettoEvento(id);
    }

    public void setImage(MultipartFile immagine, Long id) {
        Evento evento= eventoDao.findById(id).orElseThrow(() ->
                new ContentNotFoundException("location with id " + id + " not found")
        );

        try {
            evento.setImmagine(immagine.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        eventoDao.save(evento);
    }

    public byte[] getImage(Long id) {
        Evento evento= eventoDao.findById(id).orElseThrow(() ->
                new ContentNotFoundException("location with id " + id + " not found")
        );

        return evento.getImmagine();
    }
}