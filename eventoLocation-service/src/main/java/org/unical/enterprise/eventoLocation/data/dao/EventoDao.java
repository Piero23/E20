package org.unical.enterprise.eventoLocation.data.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.unical.enterprise.eventoLocation.data.entities.Evento;


public interface EventoDao extends JpaRepository<Evento, Long> {

    Page<Evento> findAll(Pageable pageable);
}
