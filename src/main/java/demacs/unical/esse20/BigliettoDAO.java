package demacs.unical.esse20;

import demacs.unical.esse20.domain.Biglietto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BigliettoDAO extends JpaRepository<Biglietto, Long> {
}
