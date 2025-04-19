package demacs.unical.esse20;


import demacs.unical.esse20.data.dao.EventoDao;
import demacs.unical.esse20.data.dao.LocationDao;
import demacs.unical.esse20.data.dao.PreferitiDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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