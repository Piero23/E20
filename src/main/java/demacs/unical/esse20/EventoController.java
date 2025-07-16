package com.example.donut;

import com.example.donut.Entities.Evento;
import com.example.donut.Services.EventoService;
import com.example.donut.Services.iCal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private iCal icsService;

    // Invoke-WebRequest -Uri "http://localhost:8888/scarica-ics?uid=uid1" -OutFile "evento.ics"
    @GetMapping("/scarica-ics")
    public ResponseEntity<byte[]> scaricaIcs(@RequestParam String uid) {
        Evento evento = eventoService.getEventoByUid(uid);

        if (evento == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        byte[] icsFile = icsService.generateIcsContent(evento);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=evento.ics");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/calendar; charset=utf-8");

        return new ResponseEntity<>(icsFile, headers, HttpStatus.OK);
    }
}