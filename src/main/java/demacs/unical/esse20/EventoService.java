package demacs.unical.esse20;


import demacs.unical.esse20.dao.BigliettoDAO;
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
    private final BigliettoDAO bigliettoDao;

    @Transactional(readOnly = true)
    public void stampa() {
        Ordine ordine =  new Ordine(1L, 5, 10.5F, new Date(10,10,10));

        Biglietto b = new Biglietto(2L,"AAAAAAAAAAAAAAAAAAAAAAAAAAAAA",true,"cia","caa",new Date(10,10,10));

        ordine.addBiglietto(b);

    }
}