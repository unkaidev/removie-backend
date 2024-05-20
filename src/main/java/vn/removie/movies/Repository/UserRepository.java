package vn.removie.movies.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.removie.movies.Entity.Movie;
import vn.removie.movies.Entity.User;

import java.util.Optional;
@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    User findFirstByOrderByIdDesc();
}
