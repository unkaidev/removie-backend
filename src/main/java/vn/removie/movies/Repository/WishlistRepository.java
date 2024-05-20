package vn.removie.movies.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.removie.movies.Entity.Wishlist;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, ObjectId> {
}
