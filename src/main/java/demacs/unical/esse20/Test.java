package demacs.unical.esse20;

import demacs.unical.esse20.dao.UtenteDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Test implements CommandLineRunner {

    EventoService eventoService;

    public Test (EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @Override
    public void run(String... args) throws Exception {

        eventoService.stampa();

    }

}
