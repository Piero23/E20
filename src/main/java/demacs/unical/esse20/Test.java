package demacs.unical.esse20;

import demacs.unical.esse20.dao.BigliettoDao;
import demacs.unical.esse20.dao.OrdineDao;
import demacs.unical.esse20.service.BigliettoService;
import demacs.unical.esse20.service.OrdineService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class Test implements CommandLineRunner {


    OrdineDao ordineDao ;
    BigliettoDao bigliettoDao ;

    public Test ( OrdineDao ordineDao, BigliettoDao bigliettoDao) {
        this.ordineDao = ordineDao;
        this.bigliettoDao = bigliettoDao;
    }

    @Override
    public void run(String... args) throws Exception {


    }
}
