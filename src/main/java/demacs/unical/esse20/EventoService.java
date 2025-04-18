package demacs.unical.esse20;


import demacs.unical.esse20.dao.OrdineDao;
import demacs.unical.esse20.dao.UtenteDao;
import demacs.unical.esse20.domain.Biglietto;
import demacs.unical.esse20.domain.Ordine;
import demacs.unical.esse20.domain.Utente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final OrdineDao ordineDao;
    private final UtenteDao utenteDao;

    @Transactional(readOnly = true)
    public void stampa() {

        Utente u = new Utente("Us","adofuigjoaed",true,"cia", LocalDate.of(10, 10, 10));

        utenteDao.save(u);



        Ordine ordine =  new Ordine(u, 5L, 10.5F, new Date());

        Biglietto b = new Biglietto(2L,"AAAAAAAAAAAAAAAAAAAAAAAAAAAAA",true,"cia","caa",new Date(10,10,10));

        ordine.addBiglietto(b);

        ordineDao.save(ordine);
    }


}