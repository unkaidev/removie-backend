package vn.removie.movies.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.removie.movies.Entity.Credit;
import vn.removie.movies.Entity.Movie;
import vn.removie.movies.Service.CreditService;
import vn.removie.movies.Service.MovieService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private CreditService creditService;

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return new ResponseEntity<List<Movie>>(movieService.allMovies(), HttpStatus.OK);
    }

    @GetMapping("/{imdbId}")
    public ResponseEntity<Optional<Movie>> getSingleMovie(@PathVariable String imdbId) {
        return new ResponseEntity<Optional<Movie>>(movieService.singleMovie(imdbId), HttpStatus.OK);
    }

    @GetMapping("/{imdbId}/credit")
    public ResponseEntity<Optional<Credit>> getCreditInSingleMovie(@PathVariable String imdbId) {
        return new ResponseEntity<Optional<Credit>>(creditService.singleMovieCredit(imdbId), HttpStatus.OK);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<Page<Movie>> getAllMoviesByReleaseDate(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "1") int page
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Movie> moviePage = movieService.findPaginatedByReleaseDateDesc(pageable);

        return new ResponseEntity<>(moviePage, HttpStatus.OK);
    }

    @GetMapping("/popularity")
    public ResponseEntity<Page<Movie>> getAllMoviesByPopularity(
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "1") int page
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Movie> moviePage = movieService.findPaginatedByPopularity(pageable);

        return new ResponseEntity<>(moviePage, HttpStatus.OK);
    }

    @GetMapping("/trending/{dateInput}")
    public ResponseEntity<Page<Movie>> getTrendingMovies(
            @PathVariable String dateInput,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "1") int page) {

        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<Movie> moviePage = movieService.findTrendingMovies(dateInput, pageable);

        if (moviePage != null) {
            return new ResponseEntity<>(moviePage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/search/{title}")
    public Page<Movie> searchMoviesByTitle(
            @PathVariable String title,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "4") int size) {

        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        return movieService.findPaginatedByTitle(title, pageRequest);
    }

    @GetMapping("/{imdbId}/rating")
    public ResponseEntity<Float> getRating(@PathVariable String imdbId) {
        Float rating = movieService.getRating(imdbId);
        if (rating != null) {
            return new ResponseEntity<>(rating, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("/{imdbId}/rating")
    public ResponseEntity<String> rateMovie(@PathVariable String imdbId, @RequestParam float rating) {
        movieService.updateRating(imdbId, rating);
        return new ResponseEntity<>("Rating updated successfully", HttpStatus.OK);
    }

}
