package demacs.unical.esse20.service;


import demacs.unical.esse20.dao.OrdineDao;
import demacs.unical.esse20.domain.Biglietto;
import demacs.unical.esse20.domain.Ordine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrdineService {

    private final OrdineDao ordineDao;

    @Transactional(readOnly = true)
    public void test() {
/*
        Ordine ordine =  new Ordine("117c2dcb-d492-4dd6-b349-8db6a021038c", 5, 10.5F, new Date(10,10,10));

        Biglietto b = new Biglietto(2L,"AAAAAAAAAAAAAAAAAAAAAAAAAAAAA",true,"cia","caa",new Date(10,10,10));

        ordine.addBiglietto(b);

        ordineDao.save(ordine);


 */
    }

    @Transactional
    public void saveBiglietti(Ordine o, Set<Biglietto> b) {
        o.getBiglietti().addAll(b);
        ordineDao.save(o);
    }
}