package org.unical.enterprise.eventoLocation.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.unical.enterprise.eventoLocation.ContentNotFoundException;
import org.unical.enterprise.eventoLocation.data.dao.EventoDao;
import org.unical.enterprise.eventoLocation.data.dao.PreferitiDao;
import org.unical.enterprise.eventoLocation.data.dto.PreferitiDto;
import org.unical.enterprise.eventoLocation.data.entities.Preferiti;
import org.unical.enterprise.shared.clients.UtenteServiceClient;

import java.util.UUID;

@Service
@AllArgsConstructor
public class PreferitiService {

    PreferitiDao preferitiDao;
    EventoDao eventoDao;
    UtenteServiceClient utenteServiceClient;

    public PreferitiDto getById(Long id){
        return new PreferitiDto(preferitiDao.findById(id).orElseThrow(() ->
                new ContentNotFoundException("preferiti with id " + id + " not found")
        ));
    }

    public void save(PreferitiDto preferitiDto){

        //TODO exception
        utenteServiceClient.getById(UUID.fromString(preferitiDto.getUtente_id()));

        preferitiDao.save(
                new Preferiti(
                        preferitiDto.getUtente_id(),

                        eventoDao.findById(preferitiDto.getEvento_id())
                                .orElseThrow( () ->new ContentNotFoundException("evento with id " + preferitiDto.getEvento_id() + " not found"))
                )
        );

    }

    public void delete(Long id){

        if(preferitiDao.findById(id).isEmpty())
            throw new ContentNotFoundException("preferiti with id "+ id + "not found");

        preferitiDao.deleteById(id);
    }

}
