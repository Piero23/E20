package demacs.unical.esse20.service;

import demacs.unical.esse20.data.dao.LocationDao;
import demacs.unical.esse20.data.dto.LocationDto;
import demacs.unical.esse20.data.entities.Location;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LocationService {

    LocationDao locationDao;

    public Location getById(Long id){
        return locationDao.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    public Location save(Location location){
        return locationDao.save(location);
    }

    public Location update(Location location , Long id){

        locationDao.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Location non trovata")
        );

        location.setId(id);

        return locationDao.save(location);
    }

    public void delete(Long id){

        if(locationDao.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);

        locationDao.deleteById(id);
    }

    /*
    public String getNomeById(long id){

    };


    public Collection<Location> getAll(){

    };

    public List<LocationDto> getCourseTeacherDto(){

    };

 */
}
