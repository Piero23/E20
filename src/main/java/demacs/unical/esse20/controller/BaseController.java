package demacs.unical.esse20.controller;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


public abstract class BaseController<C,K,D>{


    protected JpaRepository<C, K> repository;

    public BaseController(JpaRepository<C,K> repository) {
        this.repository = repository;
    }

    /*
    @GetMapping("/{id}")
    public ResponseEntity<C> findById(@PathVariable K id) {

        C obj = repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "non trovata")
        );

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }


     */

    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvento(@PathVariable("id") K id) {
        repository.deleteById(id);
    }

}
