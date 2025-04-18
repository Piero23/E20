package demacs.unical.esse20.dao;

import demacs.unical.esse20.domain.Biglietto;
import demacs.unical.esse20.domain.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BigliettoDao extends JpaRepository<Biglietto, Long> {

    List<Biglietto> findAllByOrdine(Ordine ordine);
}
