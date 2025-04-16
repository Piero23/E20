package demacs.unical.esse20;

import demacs.unical.esse20.dao.BigliettoDAO;
import demacs.unical.esse20.dao.OrdineDao;
import demacs.unical.esse20.dao.UtenteDao;
import demacs.unical.esse20.domain.Biglietto;
import demacs.unical.esse20.domain.Ordine;
import demacs.unical.esse20.domain.Utente;
import org.apache.catalina.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Test implements CommandLineRunner {


    private final UtenteDao utenteDao;
    UtenteDao userDao ;
    OrdineDao ordineDao ;
    BigliettoDAO bigliettoDao ;

    public Test (UtenteDao userDao, OrdineDao ordineDao, BigliettoDAO bigliettoDao, UtenteDao utenteDao) {
        this.userDao = userDao;
        this.ordineDao = ordineDao;
        this.bigliettoDao = bigliettoDao;
        this.utenteDao = utenteDao;
    }

    @Override
    public void run(String... args) throws Exception {

        EventoService a = new EventoService(ordineDao, utenteDao);

        a.stampa();

    }
}
