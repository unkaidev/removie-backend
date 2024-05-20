package vn.removie.movies.BE.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.removie.movies.Entity.Cast;
import vn.removie.movies.Entity.Crew;
import vn.removie.movies.Repository.CastRepository;
import vn.removie.movies.Repository.CrewRepository;

@RestController
@RequestMapping("/api/v1/credit/")
public class apiBEController {
    @Autowired
    private CastRepository castRepository;
    @Autowired
    private CrewRepository crewRepository;


    @PostMapping("/cast")
    public ResponseEntity<?> createCast(@RequestBody Cast newCast) {
        try {
            Cast savedCast = castRepository.save(newCast);
            return new ResponseEntity<>(savedCast, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create cast: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/crew")
    public ResponseEntity<?> createCrew(@RequestBody Crew newCrew) {
        try {
            Crew savedCrew = crewRepository.save(newCrew);
            return new ResponseEntity<>(savedCrew, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create crew: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
