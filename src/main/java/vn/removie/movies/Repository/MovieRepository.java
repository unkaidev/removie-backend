package vn.removie.movies.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.removie.movies.Entity.Movie;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends MongoRepository<Movie, ObjectId> {
    Optional<Movie> findMovieByImdbId(String imdbId);

    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Movie findFirstByOrderByIdDesc();

    Movie findFirstByOrderByReleaseDateDesc();
}
