package demacs.unical.esse20;

import demacs.unical.esse20.domain.Evento;
import demacs.unical.esse20.domain.Utente;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.Optional;

@Component
public class Test implements CommandLineRunner {


    private final EventoDao eventoDao;
    UtenteDao userDao ;

    public Test (UtenteDao userDao, EventoDao eventoDao) {
        this.userDao = userDao;
        this.eventoDao = eventoDao;
    }

    @Override
    public void run(String... args) throws Exception {
        Utente u= new Utente("ebrei", "cancello", true, "pass", new Date());

        Evento e= new Evento("desc", 123412L, "event", 5L, false, false);

        try{
            eventoDao.save(e);
            userDao.save(u);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        eventoDao.findAll().forEach(System.out::println);

        /*Optional<Evento> evento = eventoDao.findById(1L);

        if(evento.isPresent()){
            //System.out.println(utente.get().getAmici());
        } else System.out.println("AAA");


         */
    }
}
