package demacs.unical.esse20;

import demacs.unical.esse20.dao.BigliettoDAO;
import demacs.unical.esse20.dao.OrdineDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Test implements CommandLineRunner {


    OrdineDao ordineDao ;
    BigliettoDAO bigliettoDao ;

    public Test ( OrdineDao ordineDao, BigliettoDAO bigliettoDao) {
        this.ordineDao = ordineDao;
        this.bigliettoDao = bigliettoDao;
    }

    @Override
    public void run(String... args) throws Exception {

        EventoService a = new EventoService(ordineDao,bigliettoDao);

        a.stampa();


    }
}
