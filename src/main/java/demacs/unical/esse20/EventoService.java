package demacs.unical.esse20;


import demacs.unical.esse20.dao.EventoDao;
import demacs.unical.esse20.dao.LocationDao;
import demacs.unical.esse20.dao.PreferitiDao;
import demacs.unical.esse20.domain.Evento;
import demacs.unical.esse20.domain.Location;
import demacs.unical.esse20.domain.Preferiti;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class EventoService {


    EventoDao eventoDao;
    LocationDao locationDao;
    PreferitiDao preferitiDao;

    public EventoService(EventoDao eventoDao, LocationDao locationDao, PreferitiDao preferitiDao) {
        this.eventoDao = eventoDao;
        this.locationDao = locationDao;
        this.preferitiDao = preferitiDao;
    }

    @Transactional(readOnly = true)
    public void stampa() {


        /*
        Evento evento = new Evento("MI piace tantffffissimo la paaaaallee",location,"adaffffda","123456789012345678901234567890123456",35L,true,true);
        eventoDao.save(evento);

        TODO aggiungere data ad evento
         */




    }


}