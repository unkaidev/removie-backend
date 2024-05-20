package vn.removie.movies.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.removie.movies.Entity.Credit;
import vn.removie.movies.Entity.Movie;

import java.util.Optional;
@Repository
public interface CreditRepository extends MongoRepository<Credit,ObjectId> {
    Optional<Credit> findCreditByMovie(Optional<Movie> movie);

    Page<Credit> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Optional<Credit> findById(String id);
}
