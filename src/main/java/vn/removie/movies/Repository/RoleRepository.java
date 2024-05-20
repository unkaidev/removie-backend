package vn.removie.movies.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.removie.movies.Entity.ERole;
import vn.removie.movies.Entity.Role;

import java.util.Optional;
@Repository
public interface RoleRepository extends MongoRepository<Role,String> {
    Optional<Role> findByName(ERole name);

}
