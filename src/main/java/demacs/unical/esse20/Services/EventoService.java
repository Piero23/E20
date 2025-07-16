package com.example.donut.Services;

import com.example.donut.Entities.Evento;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EventoService {

    private final Map<String, Evento> eventi = new HashMap<>();
    public EventoService() {
        // evento di esempio all’avvio
        eventi.put("uid1", new Evento(
                "uid1",
                "Mario Rossi",
                "mario.rossi@example.com",
                "20250519T170000Z",
                "20250525T100000Z",
                "20250525T120000Z",
                "Riunione Importante"
        ));
    }

    public Evento getEventoByUid(String uid) {
        return eventi.get(uid);
    }
}
