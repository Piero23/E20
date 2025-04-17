package demacs.unical.esse20;


import demacs.unical.esse20.dao.UtenteDao;
import demacs.unical.esse20.domain.Utente;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final UtenteDao utenteDao;

    @Transactional
    public void stampa() {

        Utente utente = utenteDao.findById(1L).get();

        utente.addAmico(utenteDao.findById(2L).get());

        utenteDao.save(utente);
    }


}