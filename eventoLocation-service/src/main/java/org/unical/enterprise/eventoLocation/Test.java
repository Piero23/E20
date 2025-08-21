package org.unical.enterprise.eventoLocation;

import org.unical.enterprise.eventoLocation.data.dao.EventoDao;
import org.unical.enterprise.eventoLocation.data.dao.LocationDao;
import org.unical.enterprise.eventoLocation.data.dao.PreferitiDao;
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


    }
}
