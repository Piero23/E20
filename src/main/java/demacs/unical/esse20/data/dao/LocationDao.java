package demacs.unical.esse20.data.dao;

import demacs.unical.esse20.data.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationDao extends JpaRepository<Location, Long> {
}
