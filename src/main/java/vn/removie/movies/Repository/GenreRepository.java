package vn.removie.movies.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.removie.movies.Entity.Genre;
@Repository
public interface GenreRepository extends MongoRepository<Genre,String> {
}
