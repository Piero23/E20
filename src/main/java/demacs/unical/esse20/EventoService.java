package demacs.unical.esse20;

import demacs.unical.esse20.dao.BigliettoDAO;
import demacs.unical.esse20.dao.EventoDao;
import demacs.unical.esse20.dao.OrdineDao;
import demacs.unical.esse20.domain.Biglietto;
import demacs.unical.esse20.domain.Ordine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final OrdineDao ordineDao;

    @Transactional(readOnly = true)
    public void stampa() {
           /*
        Utente u = new Utente("Us","cajpd",true,"cia",new Date(10,10,10));

        utenteDao.save(u);
        */

        Ordine ordine =  ordineDao.findById(1).get();

        Biglietto b = new Biglietto(2L,"AAAAAAAAAAAAAAAAAAAAAAAAAAAAA",true,"cia","caa",new Date(10,10,10));

        ordine.addBiglietto(b);

        ordineDao.save(ordine);
    }


}