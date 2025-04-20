package demacs.unical.esse20.service;

import demacs.unical.esse20.dao.BigliettoDao;
import demacs.unical.esse20.domain.Biglietto;
import demacs.unical.esse20.domain.Ordine;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BigliettoService {

    private final BigliettoDao bigliettoDao;

    @Transactional
    public List<Biglietto> findAllByOrdine(Ordine ordine){
        return bigliettoDao.findAllByOrdine(ordine);
    }
}
