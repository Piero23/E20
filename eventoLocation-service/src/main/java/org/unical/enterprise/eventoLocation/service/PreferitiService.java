package org.unical.enterprise.eventoLocation.service;

import org.springframework.transaction.annotation.Transactional;
import org.unical.enterprise.eventoLocation.ContentNotFoundException;
import org.unical.enterprise.eventoLocation.data.dao.EventoDao;
import org.unical.enterprise.eventoLocation.data.dao.PreferitiDao;
import org.unical.enterprise.eventoLocation.data.dto.PreferitiDto;
import org.unical.enterprise.eventoLocation.data.entities.Evento;
import org.unical.enterprise.eventoLocation.data.entities.Preferiti;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.unical.enterprise.shared.clients.UtenteServiceClient;
import org.unical.enterprise.shared.dto.EventoBasicDto;

import java.util.List;
import java.util.Optional;
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

    // Getter Methods
    public List<EventoBasicDto> getAllEventiByUtenteId(String utenteUUID) {
        return preferitiDao.findAllByUtenteId(utenteUUID)
                .stream()
                .map(Preferiti::getEvento)
                .map(Evento::toSharedDTO)
                .toList();
    }

    public void findAndDelete(PreferitiDto preferitiDto) {
        Optional<Preferiti> relazionePreferiti;

        if (preferitiDto.getId() > 0) relazionePreferiti = preferitiDao.findById(preferitiDto.getId());
        else relazionePreferiti = preferitiDao.findByUtenteIdAndEventoId(preferitiDto.getUtente_id(), preferitiDto.getEvento_id());

        relazionePreferiti.ifPresent(preferiti -> preferitiDao.delete(preferiti));

    }

    @Transactional
    public void deleteListaUtente(String utenteUUID) {
        try { preferitiDao.deleteAllByUtenteId(utenteUUID); }
        catch (Exception e) { throw new RuntimeException("Eliminazione Lista Preferiti Fallita"); }
    }

}
