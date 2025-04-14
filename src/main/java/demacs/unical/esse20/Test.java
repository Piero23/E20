package demacs.unical.esse20;

import demacs.unical.esse20.domain.Utente;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.Optional;

@Component
public class Test implements CommandLineRunner {


    UtenteDao userDao ;

    public Test (UtenteDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void run(String... args) throws Exception {

        Optional<Utente> utente = userDao.findById(1L);

        if(utente.isPresent()){
            //System.out.println(utente.get().getAmici());
        } else System.out.println("AAA");

    }
}
