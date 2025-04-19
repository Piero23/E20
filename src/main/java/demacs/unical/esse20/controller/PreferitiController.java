package demacs.unical.esse20.controller;

import demacs.unical.esse20.data.dao.PreferitiDao;
import demacs.unical.esse20.data.dto.PreferitiDto;
import demacs.unical.esse20.data.entities.Preferiti;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/preferiti")
@AllArgsConstructor
public class PreferitiController {

    private final PreferitiDao preferitiDao;


    @GetMapping(value="/{id}")
    private ResponseEntity<PreferitiDto> findById(@PathVariable("id") Long id){
        if(preferitiDao.findById(id).isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(new PreferitiDto(preferitiDao.findById(id).get()));
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Preferiti> createEvento(@RequestBody Preferiti preferiti) {
        return new ResponseEntity<>(preferitiDao.save(preferiti), HttpStatus.CREATED);
    }



    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvento(@PathVariable("id") Long id) {
        preferitiDao.deleteById(id);
    }



    @PutMapping(path="/{id}", consumes = "application/json")
    public ResponseEntity<Preferiti> replacePerson(@PathVariable("id") Long id, @RequestBody Preferiti preferiti) {

        preferitiDao.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Location non trovata")
        );
        preferiti.setId(id);

        return new ResponseEntity<>(preferitiDao.save(preferiti), HttpStatus.OK);
    }

}

/* Post API
{
    "utente_id": "Bla Bla Bla Bla Bla Bla Bla Bla Bla",
    "evento_id": 1,
    "status" : false
}
 */