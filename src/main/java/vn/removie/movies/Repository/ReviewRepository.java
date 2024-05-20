package vn.removie.movies.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.removie.movies.Entity.Review;

@Repository
public interface ReviewRepository extends MongoRepository<Review, ObjectId> {
}
