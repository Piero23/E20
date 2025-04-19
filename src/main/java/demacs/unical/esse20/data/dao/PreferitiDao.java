package demacs.unical.esse20.data.dao;

import demacs.unical.esse20.data.entities.Preferiti;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;


public interface PreferitiDao extends JpaRepository<Preferiti, Long> {
}
