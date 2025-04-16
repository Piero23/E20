package demacs.unical.esse20;

import demacs.unical.esse20.dao.EventoDao;
import demacs.unical.esse20.dao.LocationDao;
import demacs.unical.esse20.dao.PreferitiDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Test implements CommandLineRunner {


    EventoDao eventoDao;
    LocationDao locationDao;
    PreferitiDao preferitiDao;

    public Test (EventoDao eventoDao, LocationDao locationDao, PreferitiDao preferitiDao) {
        this.eventoDao = eventoDao;
        this.locationDao = locationDao;
        this.preferitiDao = preferitiDao;
    }

    @Override
    public void run(String... args) throws Exception {

        EventoService a = new EventoService(eventoDao,locationDao,preferitiDao);

        a.stampa();

    }
}
