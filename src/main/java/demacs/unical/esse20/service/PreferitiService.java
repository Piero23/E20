package demacs.unical.esse20.service;

import demacs.unical.esse20.ContentNotFoundException;
import demacs.unical.esse20.data.dao.EventoDao;
import demacs.unical.esse20.data.dao.PreferitiDao;
import demacs.unical.esse20.data.dto.PreferitiDto;
import demacs.unical.esse20.data.entities.Location;
import demacs.unical.esse20.data.entities.Preferiti;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class PreferitiService {

    PreferitiDao preferitiDao;
    EventoDao eventoDao;

    public PreferitiDto getById(Long id){
        return new PreferitiDto(preferitiDao.findById(id).orElseThrow(() ->
                new ContentNotFoundException("preferiti with id " + id + " not found")
        ));
    }

    public void save(PreferitiDto preferitiDto){

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
