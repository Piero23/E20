package demacs.unical.esse20.controller;

import demacs.unical.esse20.dao.PreferitiDao;
import demacs.unical.esse20.domain.Location;
import demacs.unical.esse20.domain.Preferiti;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/preferiti")
@AllArgsConstructor
public class PreferitiController {

    private final PreferitiDao preferitiDao;


    @GetMapping(value = "/{id}")
    private Preferiti findById(@PathVariable("id") Long id) {
        if (preferitiDao.findById(id).isPresent())
            return preferitiDao.findById(id).get();
        return null;
    }

    @PostMapping(value = "/add")
    public boolean addLocation(@RequestBody Preferiti preferiti) {
        try {
            preferitiDao.save(preferiti);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}