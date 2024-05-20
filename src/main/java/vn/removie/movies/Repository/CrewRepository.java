package vn.removie.movies.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.removie.movies.Entity.Cast;
import vn.removie.movies.Entity.Crew;

@Repository
public interface CrewRepository extends MongoRepository<Crew, ObjectId> {
}
