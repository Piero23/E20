package demacs.unical.esse20.dao;

import demacs.unical.esse20.domain.Biglietto;
import demacs.unical.esse20.domain.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BigliettoDao extends JpaRepository<Biglietto, UUID> {

    List<Biglietto> findAllByOrdine(Ordine ordine);
}
