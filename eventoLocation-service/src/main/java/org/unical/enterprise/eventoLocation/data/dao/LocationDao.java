package org.unical.enterprise.eventoLocation.data.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.unical.enterprise.eventoLocation.data.entities.Location;

public interface LocationDao extends JpaRepository<Location, Long> {
    Page<Location> findAll(Pageable pageable);
}
