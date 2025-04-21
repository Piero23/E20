package demacs.unical.esse20.controller;

import demacs.unical.esse20.data.dao.EventoDao;
import demacs.unical.esse20.data.dao.PreferitiDao;
import demacs.unical.esse20.data.dto.PreferitiDto;
import demacs.unical.esse20.data.entities.Evento;
import demacs.unical.esse20.data.entities.Preferiti;
import demacs.unical.esse20.service.PreferitiService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/preferiti")
@AllArgsConstructor
public class PreferitiController {

    private final PreferitiService preferitiService;

    @GetMapping(value="/{id}")
    private ResponseEntity<?> findById(@PathVariable("id") Long id){
        return new ResponseEntity<>(preferitiService.getById(id), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createPreferito(@RequestBody PreferitiDto preferiti) {
        preferitiService.save(preferiti);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePreferito(@PathVariable("id") Long id) {
        preferitiService.delete(id);
    }
}

/* Post API
{
    "utente_id": "Bla Bla Bla Bla Bla Bla Bla Bla Bla",
    "evento_id": 1,
    "status" : false
}
 */